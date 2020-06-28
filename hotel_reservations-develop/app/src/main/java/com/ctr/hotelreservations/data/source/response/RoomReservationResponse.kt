package com.ctr.hotelreservations.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */
@Parcelize
data class RoomReservationResponse(
    @SerializedName("body") val roomReservations: List<RoomReservation> = listOf(),
    @SerializedName("length") val length: Int? = 0
) : Parcelable {
    @Parcelize
    data class RoomReservation(
        @SerializedName("email") val email: String? = "",
        @SerializedName("endDate") val endDate: String? = "",
        @SerializedName("firstName") val firstName: String? = "",
        @SerializedName("id") val id: Int? = 0,
        @SerializedName("lastName") val lastName: String? = "",
        @SerializedName("reservation") val reservation: Reservation? = Reservation(),
        @SerializedName("room") val room: Room? = Room(),
        @SerializedName("startDate") val startDate: String? = "",
        @SerializedName("status") val status: String? = ""
    ) : Parcelable {
        @Parcelize
        data class Reservation(
            @SerializedName("id") val id: Int? = 0,
            @SerializedName("promos") val promos: List<Promo?>? = listOf(),
            @SerializedName("status") val status: String? = "",
            @SerializedName("taxPercent") val taxPercent: Double? = 0.0,
            @SerializedName("totalAfterTax") val totalAfterTax: Double? = 0.0,
            @SerializedName("totalBeforeTax") val totalBeforeTax: Double? = 0.0,
            @SerializedName("user") val user: User? = User()
        ) : Parcelable {
            @Parcelize
            data class Promo(
                @SerializedName("description") val description: String? = "",
                @SerializedName("dollarDiscount") val dollarDiscount: Double? = 0.0,
                @SerializedName("endDate") val endDate: String? = "",
                @SerializedName("id") val id: Int? = 0,
                @SerializedName("percentDiscount") val percentDiscount: Double? = 0.0,
                @SerializedName("promoCode") val promoCode: String? = "",
                @SerializedName("roomType") val roomType: RoomType? = RoomType(),
                @SerializedName("startDate") val startDate: String? = ""
            ) : Parcelable {
                @Parcelize
                data class RoomType(
                    @SerializedName("capacity") val capacity: Double? = 0.0,
                    @SerializedName("description") val description: String? = "",
                    @SerializedName("extras") val extras: List<Extra?>? = listOf(),
                    @SerializedName("id") val id: Int? = 0,
                    @SerializedName("images") val images: List<Image?>? = listOf(),
                    @SerializedName("name") val name: String? = "",
                    @SerializedName("price") val price: Double? = 0.0,
                    @SerializedName("size") val size: Double? = 0.0,
                    @SerializedName("thumbnail") val thumbnail: String? = "",
                    @SerializedName("type") val type: String? = ""
                ) : Parcelable {
                    @Parcelize
                    data class Extra(
                        @SerializedName("id") val id: Int? = 0,
                        @SerializedName("name") val name: String? = ""
                    ) : Parcelable

                    @Parcelize
                    data class Image(
                        @SerializedName("id") val id: Int? = 0,
                        @SerializedName("name") val name: String? = ""
                    ) : Parcelable
                }
            }

            @Parcelize
            data class User(
                @SerializedName("birthday") val birthday: String? = "",
                @SerializedName("email") val email: String? = "",
                @SerializedName("firstName") val firstName: String? = "",
                @SerializedName("id") val id: Int? = 0,
                @SerializedName("lastName") val lastName: String? = "",
                @SerializedName("phone") val phone: String? = "",
                @SerializedName("roleEntities") val roleEntities: List<RoleEntity?>? = listOf(),
                @SerializedName("status") val status: String? = ""
            ) : Parcelable {
                @Parcelize
                data class RoleEntity(
                    @SerializedName("id") val id: Int? = 0,
                    @SerializedName("name") val name: String? = ""
                ) : Parcelable
            }
        }

        @Parcelize
        data class Room(
            @SerializedName("floor") val floor: Int? = 0,
            @SerializedName("id") val id: Int? = 0,
            @SerializedName("name") val name: String? = ""
        ) : Parcelable
    }
}
