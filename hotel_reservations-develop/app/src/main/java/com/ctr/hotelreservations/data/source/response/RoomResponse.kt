package com.ctr.hotelreservations.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
@Parcelize
data class RoomResponse(
    @SerializedName("body") val rooms: List<Room> = listOf(),
    @SerializedName("length") val length: Int = 0
) : Parcelable {
    @Parcelize
    data class Room(
        @SerializedName("brand") val brand: Brand = Brand(),
        @SerializedName("floor") val floor: Int = 0,
        @SerializedName("id") val id: Int = 0,
        @SerializedName("name") val name: String = "",
        @SerializedName("roomReservationDTOList") val roomReservationDTOList: List<RoomReservationDTO> = listOf(),
        @SerializedName("roomType") val roomType: RoomType = RoomType()
    ) : Parcelable {
        @Parcelize
        data class Brand(
            @SerializedName("address") val address: String = "",
            @SerializedName("floor") val floor: Int = 0,
            @SerializedName("id") val id: Int = 0,
            @SerializedName("imgLink") val imgLink: String = "",
            @SerializedName("name") val name: String = ""
        ) : Parcelable

        @Parcelize
        data class RoomReservationDTO(
            @SerializedName("endDate") val endDate: String = "",
            @SerializedName("id") val id: Int = 0,
            @SerializedName("reservation") val reservation: Reservation = Reservation(),
            @SerializedName("room") val room: Room = Room(),
            @SerializedName("startDate") val startDate: String = "",
            @SerializedName("status") val status: String = "",
            @SerializedName("users") val users: List<User> = listOf(),
            @SerializedName("usersBooking") val usersBooking: UsersBooking = UsersBooking()
        ) : Parcelable {
            @Parcelize
            data class Reservation(
                @SerializedName("id") val id: Int = 0
            ) : Parcelable

            @Parcelize
            data class Room(
                @SerializedName("floor") val floor: Int = 0,
                @SerializedName("id") val id: Int = 0,
                @SerializedName("name") val name: String = ""
            ) : Parcelable

            @Parcelize
            data class User(
                @SerializedName("birthday") val birthday: String = "",
                @SerializedName("email") val email: String = "",
                @SerializedName("firstName") val firstName: String = "",
                @SerializedName("id") val id: Int = 0,
                @SerializedName("lastName") val lastName: String = "",
                @SerializedName("phone") val phone: String = "",
                @SerializedName("roleEntities") val roleEntities: List<RoleEntity> = listOf(),
                @SerializedName("status") val status: String = ""
            ) : Parcelable {
                @Parcelize
                data class RoleEntity(
                    @SerializedName("id") val id: Int = 0,
                    @SerializedName("name") val name: String = ""
                ) : Parcelable
            }

            @Parcelize
            data class UsersBooking(
                @SerializedName("birthday") val birthday: String = "",
                @SerializedName("email") val email: String = "",
                @SerializedName("firstName") val firstName: String = "",
                @SerializedName("id") val id: Int = 0,
                @SerializedName("lastName") val lastName: String = "",
                @SerializedName("phone") val phone: String = "",
                @SerializedName("roleEntities") val roleEntities: List<RoleEntity> = listOf(),
                @SerializedName("status") val status: String = ""
            ) : Parcelable {
                @Parcelize
                data class RoleEntity(
                    @SerializedName("id") val id: Int = 0,
                    @SerializedName("name") val name: String = ""
                ) : Parcelable
            }
        }

        @Parcelize
        data class RoomType(
            @SerializedName("capacity") val capacity: Int = 0,
            @SerializedName("description") val description: String = "",
            @SerializedName("id") val id: Int = 0,
            @SerializedName("name") val name: String = "",
            @SerializedName("price") val price: Int = 0,
            @SerializedName("size") val size: Int = 0,
            @SerializedName("thumbnail") val thumbnail: String = "",
            @SerializedName("type") val type: String = ""
        ) : Parcelable
    }
}
