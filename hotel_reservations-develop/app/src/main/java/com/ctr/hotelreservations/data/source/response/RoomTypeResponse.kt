package com.ctr.hotelreservations.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2
 */
@Parcelize
data class RoomTypeResponse(
    @SerializedName("body") val roomTypeStatusList: List<RoomTypeStatus> = listOf(),
    @SerializedName("length") val length: Int = 0
) : Parcelable {
    @Parcelize
    data class RoomTypeStatus(
        @SerializedName("roomType") val roomType: RoomType = RoomType(),
        @SerializedName("totalRoom") val totalRoom: Int = 0,
        @SerializedName("totalRoomAvailable") val totalRoomAvailable: Int = 0
    ) : Parcelable {
        @Parcelize
        data class RoomType(
            @SerializedName("capacity") val capacity: Int = 0,
            @SerializedName("description") val description: String = "",
            @SerializedName("extras") val extras: List<Extra> = listOf(),
            @SerializedName("id") val id: Int = 0,
            @SerializedName("images") val images: List<Image> = listOf(),
            @SerializedName("name") val name: String = "",
            @SerializedName("price") val price: Int = 0,
            @SerializedName("size") val size: Int = 0,
            @SerializedName("thumbnail") val thumbnail: String = "",
            @SerializedName("type") val type: String = ""
        ) : Parcelable {
            @Parcelize
            data class Extra(
                @SerializedName("id") val id: Int = 0,
                @SerializedName("name") val name: String = ""
            ) : Parcelable

            @Parcelize
            data class Image(
                @SerializedName("id") val id: Int = 0,
                @SerializedName("name") val name: String = ""
            ) : Parcelable

            fun getRoomTypeInfo(): String {
                return "$capacity guests · size $size㎡"
            }
        }
    }
}
