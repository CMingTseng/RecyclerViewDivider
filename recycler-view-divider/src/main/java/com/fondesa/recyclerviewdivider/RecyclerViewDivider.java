package com.fondesa.recyclerviewdivider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.fondesa.recycler_view_divider.R;

/**
 * Class that draws a divider between RecyclerView's elements
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration {
    private static final String TAG = "RecyclerViewDivider";

    private RecyclerViewDivider.Builder builder;

    /**
     * Set the {@link Builder} for this {@link RecyclerViewDivider}
     *
     * @param builder {@link Builder} with properties initialized
     */
    private RecyclerViewDivider(@NonNull RecyclerViewDivider.Builder builder) {
        this.builder = builder;
    }

    /**
     * Creates a new {@link Builder} for the current context
     *
     * @param context current context
     * @return a new {@link Builder} instance
     */
    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    /**
     * Show this divider on the RecyclerView
     */
    public void show() {
        builder.recyclerView.addItemDecoration(this);
    }

    /**
     * Remove this divider from the RecyclerView
     */
    public void remove() {
        builder.recyclerView.removeItemDecoration(this);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawDividerBetweenItems(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            if (builder.orientation == RecyclerView.VERTICAL) {
                outRect.set(0, 0, 0, builder.size);
            } else {
                outRect.set(0, 0, builder.size, 0);
            }
        }
    }

    /**
     * Draws a divider between items based on Builder's properties
     *
     * @param c      Canvas to draw views on
     * @param parent RecyclerView added to the builder
     */
    private void drawDividerBetweenItems(Canvas c, RecyclerView parent) {
        final int orientation = builder.orientation;

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        int margin = builder.marginSize;

        if (orientation == RecyclerView.VERTICAL) {
            left = parent.getPaddingLeft() + margin;
            right = parent.getWidth() - parent.getPaddingRight() - margin;
        } else {
            top = parent.getPaddingTop() + margin;
            bottom = parent.getHeight() - parent.getPaddingBottom() - margin;
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (orientation == RecyclerView.VERTICAL) {
                top = child.getBottom() + params.bottomMargin;
                bottom = top + builder.size;
            } else {
                left = child.getRight() + params.rightMargin;
                right = left + builder.size;
            }

            builder.divider.setBounds(left, top, right, bottom);
            builder.divider.draw(c);
        }
    }

    /**
     * {@link Builder} class for {@link RecyclerViewDivider}. This class can set these custom properties:
     * <ul>
     * <li><b>Size:</b> {@link #size(int)}</li>
     * <li><b>Margins:</b> {@link #marginSize(int)}</li>
     * <li><b>Color:</b> {@link #color(int)}</li>
     * <li><b>Drawable:</b> {@link #drawable(Drawable)}</li>
     * <li><b>Tint of the drawable:</b> {@link #tint(int)}</li>
     * </ul>
     */
    public static class Builder {
        private static final int INT_DEF = -1;

        private Context context;
        private RecyclerView recyclerView;
        @ColorInt
        private int color;
        @ColorInt
        private int tint;
        private Drawable drawable;
        private int size;
        private int marginSize;
        private int orientation;

        private Drawable divider;

        /**
         * Initialize this {@link Builder} with a context.
         *
         * @param context current context
         */
        public Builder(@NonNull Context context) {
            this.context = context;
            color = INT_DEF;
            tint = INT_DEF;
            size = INT_DEF;
            marginSize = INT_DEF;
        }

        /**
         * Add the {@link RecyclerViewDivider} to the {@link Builder}'s instance.
         *
         * @param recyclerView RecyclerView on which the divider will be displayed on
         * @return {@link Builder} instance
         */
        public Builder addTo(@NonNull RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        /**
         * Draws a simple space between RecyclerView's items.
         *
         * @return {@link Builder} instance
         */
        public Builder asSpace() {
            if (color != INT_DEF) {
                Log.w(TAG, "asSpace() will override effects of color()");
            }
            if (drawable != null) {
                drawable = null;
                Log.w(TAG, "asSpace() will override effects of drawable()");
            }
            if (tint != INT_DEF) {
                tint = INT_DEF;
                Log.w(TAG, "asSpace() will override effects of tint()");
            }
            color = Color.TRANSPARENT;
            return this;
        }

        /**
         * Set the divider's color. This method can't be used with {@link #drawable(Drawable)} or {@link #tint(int)}
         *
         * @param color resolved color for this divider, not a resource
         * @return {@link Builder} instance
         */
        public Builder color(@ColorInt int color) {
            if (drawable != null) {
                drawable = null;
                Log.w(TAG, "color() will override effects of drawable()");
            }
            this.color = color;
            return this;
        }

        /**
         * Set the divider's drawable. This method can't be used with {@link #color(int)}.
         * If you want to color the drawable, you have to use {@link #tint(int)} instead.
         *
         * @param drawable custom drawable for this divider
         * @return {@link Builder} instance
         */
        public Builder drawable(@NonNull Drawable drawable) {
            if (color != INT_DEF) {
                color = INT_DEF;
                Log.w(TAG, "drawable() will override effects of color()");
            }
            this.drawable = drawable;
            return this;
        }

        /**
         * Set the divider's drawable tint color. This method can't be used with {@link #color(int)} and without {@link #drawable(Drawable)}.
         * If you want to create a plain divider with a single color you can use {@link #color(int)} instead.
         *
         * @param color color that will be used as drawable's tint
         * @return {@link Builder} instance
         */
        public Builder tint(@ColorInt int color) {
            if (this.color != INT_DEF) {
                this.color = INT_DEF;
                Log.w(TAG, "tint() will override effects of color()");
            }
            if (drawable == null) {
                Log.w(TAG, "tint() you haven't used drawable() yet so this method won't have effects");
            }
            tint = color;
            return this;
        }

        /**
         * Set the divider's size. The divider's final size will depend on RecyclerView's orientation:
         * <ul>
         * <li><b>RecyclerView.VERTICAL:</b> the height will be equal to the size and the width will be equal to container's width + {@link #marginSize(int)}</li>
         * <li><b>RecyclerView.HORIZONTAL:</b> the width will be equal to the size and the height will be equal to container's height + {@link #marginSize(int)}</li>
         * </ul>
         *
         * @param size size in pixels for this divider
         * @return {@link Builder} instance
         */
        public Builder size(int size) {
            this.size = size;
            return this;
        }

        /**
         * Set the divider's margins. They will depend on RecyclerView's orientation:
         * <ul>
         * <li><b>RecyclerView.VERTICAL:</b> margins will be added equally to the left and to the right</li>
         * <li><b>RecyclerView.HORIZONTAL:</b> margins will be added equally to the top and to the bottom</li>
         * </ul>
         *
         * @param marginSize margins' size in pixels for this divider
         * @return {@link Builder} instance
         */
        public Builder marginSize(int marginSize) {
            this.marginSize = marginSize;
            return this;
        }

        /**
         * Creates a new {@link RecyclerViewDivider} with given configurations and initializes default values.
         * Default values will be initialized in two different ways if the builder uses a custom Drawable or a plain divider.
         * <br>
         * <b>Plain divider:</b>
         * <ul>
         * <li>Color: if {@link #color(int)} wasn't used, the default is #CCCCCC that can be overriden with the colors resource <i>recycler_view_divider_color</i></li>
         * <li>Size: if {@link #size(int)} wasn't used, the default is 1dp that can be overriden with the dimens resource <i>recycler_view_divider_size</i></li>
         * </ul>
         * <b>Custom Drawable:</b>
         * <ul>
         * <li>Size: if {@link #size(int)} wasn't used, the size will be determined by Drawable's intrinsic size.
         * If the size can't be determined yet, the default is 1dp that can be overriden with the dimens resource <i>recycler_view_divider_size</i></li>
         * <li>Tint: if {@link #tint(int)} wasn't used, the drawable won't be tinted</li>
         * </ul>
         * <br>
         * The orientation will be determined by LayoutManager's orientation
         *
         * @return a new {@link RecyclerViewDivider} with these {@link Builder} configurations
         */
        public RecyclerViewDivider build() {
            // get RecyclerView's orientation
            orientation = RecyclerUtils.getRecyclerViewOrientation(recyclerView);
            // init default values
            if (drawable == null) {
                // in this case a custom drawable wasn't specified
                // init default color if not specified
                if (color == INT_DEF) {
                    color = ContextCompat.getColor(context, R.color.recycler_view_divider_color);
                }
                // init default size if not specified
                if (size == INT_DEF) {
                    size = context.getResources().getDimensionPixelSize(R.dimen.recycler_view_divider_size);
                }
                // creates a custom shape drawable with this color
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                gradientDrawable.setColor(color);
                divider = gradientDrawable;
            } else {
                // in this case a custom drawable will be used
                // init default size if not specified
                if (size == INT_DEF) {
                    // get the size from the drawable's size
                    size = (orientation == RecyclerView.VERTICAL) ? drawable.getIntrinsicHeight() : drawable.getIntrinsicWidth();
                    // if a drawable hasn't an intrinsic size, such as a solid color, the method in Android SDK will return -1
                    if (size == -1) {
                        // the size can't be determined so will be initialized with default value
                        size = context.getResources().getDimensionPixelSize(R.dimen.recycler_view_divider_size);
                    }
                }
                // tint drawable if specified
                if (tint != INT_DEF) {
                    Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                    DrawableCompat.setTint(wrappedDrawable, tint);
                    divider = wrappedDrawable;
                } else {
                    divider = drawable;
                }
            }

            // creates divider for this builder
            return new RecyclerViewDivider(this);
        }
    }
}