/*
 * Copyright (c) 2017 Fondesa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fondesa.recyclerviewdivider;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * Utilities class
 */
@Deprecated
public final class RecyclerViewDividerUtils {

    private RecyclerViewDividerUtils() {
        // empty constructor to avoid initialization
    }

    /**
     * Converts a color to a Drawable
     *
     * @param color color to convert
     * @return ColorDrawable from color
     */
    @Deprecated
    public static Drawable colorToDrawable(@ColorInt int color) {
        return new ColorDrawable(color);
    }
}