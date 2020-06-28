package com.ctr.hotelreservations.data.source.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */
@Parcelize
data class RoomsReservationBody(
    @SerializedName("brandId") var brandId: Int? = 0,
    @SerializedName("email") var email: String? = null,
    @SerializedName("endDate") var endDate: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("reservation") var reservation: Reservation = Reservation(),
    @SerializedName("roomTypeId") var roomTypeId: Int? = 0,
    @SerializedName("startDate") var startDate: String? = null,
    @SerializedName("status") var status: String? = null
) : Parcelable {
    @Parcelize
    data class Reservation(
        @SerializedName("id") val id: Int? = 0
    ) : Parcelable
}
