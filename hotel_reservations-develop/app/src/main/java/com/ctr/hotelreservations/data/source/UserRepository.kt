package com.ctr.hotelreservations.data.source

import com.ctr.hotelreservations.data.source.datasource.UserDataSource
import com.ctr.hotelreservations.data.source.remote.UserRemoteDataSource
import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.data.source.request.RegisterBody

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
class UserRepository : UserDataSource {

    private val userRemoteDataSource = UserRemoteDataSource()

    override fun login(body: LoginBody) = userRemoteDataSource.login(body)

    override fun register(body: RegisterBody) = userRemoteDataSource.register(body)

    override fun getUserFollowId(userId: Int) = userRemoteDataSource.getUserFollowId(userId)
}
