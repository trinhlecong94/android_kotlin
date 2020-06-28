package com.ctr.hotelreservations.ui.home.account

import com.ctr.hotelreservations.data.source.response.UserResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface AccountVMContract {

    fun getUserId(): Int

    fun getUserInfo(): Single<UserResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>
}
