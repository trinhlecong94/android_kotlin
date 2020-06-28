package com.ctr.hotelreservations.ui.home.account

import com.ctr.hotelreservations.base.HomeContainerBaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class AccountContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() =
            AccountContainerFragment()
    }

    override fun getRootFragment() =
        AccountFragment.newInstance()
}
