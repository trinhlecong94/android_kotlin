package com.ctr.hotelreservations.util

import android.os.SystemClock
import kotlin.math.abs

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
object DelayAction {
    internal const val DELAY_TIME_1S = 1000
    internal const val DEFAULT_DELAY_TIME = 500 //ms
    private var lastClickTime: Long = 0

    internal fun action(action: () -> Unit) {
        action(DEFAULT_DELAY_TIME, action)
    }

    internal fun action(milis: Int, action: () -> Unit) {
        if (abs(SystemClock.elapsedRealtime() - lastClickTime) > milis) {
            action.invoke()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    }
}
