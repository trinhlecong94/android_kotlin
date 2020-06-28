package com.ctr.hotelreservations.ui.splash

import com.ctr.hotelreservations.data.source.datasource.LocalDataSource

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class SplashViewModel(private val localRepository: LocalDataSource) : SplashVMContract {

    override fun isFirstLunch() = localRepository.isFirstLaunch()

    override fun updateFirstLaunch() = localRepository.updateFirstLaunch()

    override fun getLoginToken() = localRepository.getAutoLoginToken()

    override fun getUserId() = localRepository.getUserId()
}
