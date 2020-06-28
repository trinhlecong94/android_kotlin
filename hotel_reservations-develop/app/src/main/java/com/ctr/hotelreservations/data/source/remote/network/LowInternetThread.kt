package com.ctr.hotelreservations.data.source.remote.network

import android.os.SystemClock
import com.ctr.hotelreservations.bus.LowInternetEvent
import com.ctr.hotelreservations.bus.RxBus

/**
 * https://taiga.fpocket.ml/project/w_ishimura-fortune-pocket-renewal/wiki/client-time-out-configuration
 */
class LowInternetThread(private val threshold: Int) : Thread() {
    private var isStopThread = false

    override fun run() {
        super.run()
        val time = SystemClock.currentThreadTimeMillis()
        while (!isStopThread) {
            if ((SystemClock.currentThreadTimeMillis() - time) / 1000 > threshold) {
                RxBus.publish(LowInternetEvent())
                cancel()
            }
        }
    }

    internal fun cancel() {
        isStopThread = true
        interrupt()
    }
}
