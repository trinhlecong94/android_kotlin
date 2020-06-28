package com.ctr.hotelreservations.data.source.request

import com.google.gson.annotations.SerializedName


/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
data class LoginBody(
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null
)