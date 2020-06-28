package com.ctr.hotelreservations.ui.home.favotite

import com.ctr.hotelreservations.base.HomeContainerBaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class FavoriteContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() =
            FavoriteContainerFragment()
    }

    override fun getRootFragment() =
        FavoriteFragment.newInstance()
}
