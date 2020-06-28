package com.ctr.hotelreservations.data.source.remote

import com.ctr.hotelreservations.data.source.datasource.UserDataSource
import com.ctr.hotelreservations.data.source.remote.network.ApiClient
import com.ctr.hotelreservations.data.source.remote.network.ApiService
import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.data.source.request.RegisterBody

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
class UserRemoteDataSource(private val apiService: ApiService = ApiClient.getInstance(null).service) :
    UserDataSource {

    override fun login(body: LoginBody) = apiService.login(body)

    override fun register(body: RegisterBody) = apiService.register(body)

    override fun getUserFollowId(userId: Int) = apiService.getUserFollowId(userId)
}
