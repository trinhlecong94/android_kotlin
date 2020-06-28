package com.ctr.hotelreservations.data.source.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/23
 */
@Parcelize
data class RegisterBody(
    @SerializedName("birthday") var birthday: String? = "1998-01-01T00:00:00.000Z",
    @SerializedName("email") var email: String? = "",
    @SerializedName("firstName") var firstName: String? = "",
    @SerializedName("lastName") var lastName: String? = "",
    @SerializedName("password") var password: String? = "",
    @SerializedName("phone") var phone: String? = ""
) : Parcelable
