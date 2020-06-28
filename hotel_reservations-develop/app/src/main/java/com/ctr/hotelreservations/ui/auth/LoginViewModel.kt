package com.ctr.hotelreservations.ui.auth

import com.ctr.hotelreservations.base.BaseViewModel
import com.ctr.hotelreservations.data.source.datasource.LocalDataSource
import com.ctr.hotelreservations.data.source.datasource.UserDataSource
import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.data.source.request.RegisterBody
import com.ctr.hotelreservations.data.source.response.LoginResponse
import com.ctr.hotelreservations.data.source.response.RegisterResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
class LoginViewModel(
    private val localRepository: LocalDataSource,
    private val userRepository: UserDataSource
) : LoginVMContract, BaseViewModel() {

    override fun login(loginBody: LoginBody): Single<LoginResponse> {
        return userRepository.login(loginBody)
            .addProgressLoading()
    }

    override fun register(registerBody: RegisterBody): Single<RegisterResponse> {
        return userRepository.register(registerBody)
            .addProgressLoading()
    }

    override fun saveAutoLoginToken(token: String?) {
        localRepository.saveAutoLoginToken(token)
    }

    override fun saveUserId(id: Int) {
        localRepository.saveUserId(id)
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
