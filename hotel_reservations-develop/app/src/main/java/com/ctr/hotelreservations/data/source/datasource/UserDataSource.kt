package com.ctr.hotelreservations.data.source.datasource

import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.data.source.request.RegisterBody
import com.ctr.hotelreservations.data.source.response.LoginResponse
import com.ctr.hotelreservations.data.source.response.RegisterResponse
import com.ctr.hotelreservations.data.source.response.UserResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
interface UserDataSource {
    fun login(body: LoginBody): Single<LoginResponse>

    fun register(body: RegisterBody): Single<RegisterResponse>

    fun getUserFollowId(userId: Int): Single<UserResponse>
}
