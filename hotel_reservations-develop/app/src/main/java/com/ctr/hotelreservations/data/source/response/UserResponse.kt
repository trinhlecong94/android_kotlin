package com.ctr.hotelreservations.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/22
 */
@Parcelize
data class UserResponse(
    @SerializedName("body")
    val body: Body = Body(),
    @SerializedName("length")
    val length: Int? = 0
) : Parcelable {
    @Parcelize
    data class Body(
        @SerializedName("birthday")
        val birthday: String? = "",
        @SerializedName("email")
        val email: String? = "",
        @SerializedName("firstName")
        val firstName: String? = "",
        @SerializedName("id")
        val id: Int? = 0,
        @SerializedName("lastName")
        val lastName: String? = "",
        @SerializedName("phone")
        val phone: String? = "",
        @SerializedName("roleEntities")
        val roleEntities: List<RoleEntity?>? = listOf(),
        @SerializedName("status")
        val status: String? = ""
    ) : Parcelable {
        @Parcelize
        data class RoleEntity(
            @SerializedName("id")
            val id: Int? = 0,
            @SerializedName("name")
            val name: String? = ""
        ) : Parcelable
    }
}
