package com.ctr.hotelreservations.ui.home.mybooking

import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.HomeContainerBaseFragment
import com.ctr.hotelreservations.data.source.response.MyBookingResponse
import com.ctr.hotelreservations.extension.addFragment
import com.ctr.hotelreservations.ui.booking.PaymentFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class MyBookingContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() =
            MyBookingContainerFragment()
    }

    override fun getRootFragment() =
        MyBookingFragment.newInstance()

    internal fun openPaymentFragment(booking: MyBookingResponse.MyBooking) {
        addFragment(getContainerId(), PaymentFragment.newInstance(booking), {
            it.setCustomAnimations(
                R.anim.anim_slide_right_in,
                0,
                0,
                R.anim.anim_slide_right_out
            )
        }, true)
    }
}
