package com.ferfalk.simplesearchview.utils

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

object DimensUtils {

    @JvmStatic
    fun convertDpToPx(dp: Number, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).roundToInt()
    }
}
