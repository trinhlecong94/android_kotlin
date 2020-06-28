package com.ctr.hotelreservations.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
@Parcelize
data class HotelResponse(
    @SerializedName("body") val hotels: List<Hotel> = listOf(),
    @SerializedName("length") val length: Int = 0
) : Parcelable {
    @Parcelize
    data class Hotel(
        @SerializedName("brands") val brands: List<Brand> = listOf(),
        @SerializedName("description") val description: String = "",
        @SerializedName("id") val id: Int = 0,
        @SerializedName("images") val images: List<Image> = listOf(),
        @SerializedName("name") val name: String = ""
    ) : Parcelable {
        @Parcelize
        data class Brand(
            @SerializedName("address") val address: String = "",
            @SerializedName("desciption") val desciption: String? = null,
            @SerializedName("floor") val floor: Int = 0,
            @SerializedName("id") val id: Int = 0,
            @SerializedName("imgLink") val imgLink: String = "",
            @SerializedName("name") val name: String = ""
        ) : Parcelable

        @Parcelize
        data class Image(
            @SerializedName("id") val id: Int = 0,
            @SerializedName("name") val name: String = ""
        ) : Parcelable
    }
}
