package com.ctr.hotelreservations.ui.home.hotels

import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.HomeContainerBaseFragment
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.extension.addFragment
import com.ctr.hotelreservations.ui.home.brands.BrandFragment
import com.ctr.hotelreservations.ui.home.rooms.RoomFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class HomeContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() =
            HomeContainerFragment()
    }

    override fun getRootFragment() = HomeFragment.getInstance()

    internal fun openBrandFragment(hotel: HotelResponse.Hotel) {
        addFragment(getContainerId(), BrandFragment.getInstance(hotel), {
            it.setCustomAnimations(
                R.anim.anim_slide_right_in,
                0,
                0,
                R.anim.anim_slide_right_out
            )
        }, true)
    }

    internal fun openRoomFragment(brand: HotelResponse.Hotel.Brand) {
        addFragment(getContainerId(), RoomFragment.getInstance(brand), {
            it.setCustomAnimations(
                R.anim.anim_slide_right_in,
                0,
                0,
                R.anim.anim_slide_right_out
            )
        }, true)
    }
}
