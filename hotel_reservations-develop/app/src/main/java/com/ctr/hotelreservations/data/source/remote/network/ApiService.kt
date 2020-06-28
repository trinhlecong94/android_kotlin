package com.ctr.hotelreservations.data.source.remote.network

import com.ctr.hotelreservations.data.model.BookingStatus
import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.data.source.request.RegisterBody
import com.ctr.hotelreservations.data.source.request.RoomsReservationBody
import com.ctr.hotelreservations.data.source.response.*
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {

    @POST("/api/auth/login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse>

    @POST("/api/auth/register")
    fun register(@Body registerBody: RegisterBody): Single<RegisterResponse>

    @GET("/api/users/{id}")
    fun getUserFollowId(@Path("id") userId: Int): Single<UserResponse>

    @GET("/api/hotels")
    fun getHotels(): Single<HotelResponse>

    @GET("/api/rooms/brand/{id}")
    fun getAllRoomByBrand(@Path("id") brandId: Int): Single<RoomResponse>

    @GET("/api/rooms/status/?")
    fun getAllRoomStatus(
        @Query("brandId") brandId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<RoomTypeResponse>

    @GET("api/promos/active")
    fun getAllPromoStillActive(): Single<PromoResponse>

    @POST("/api/room-reservations?")
    fun addNewRoomsReservation(
        @Query("numberOfRooms") numberOfRooms: Int,
        @Query("listPromoCode") listPromoCode: List<String>?,
        @Body roomsReservationBody: RoomsReservationBody
    ): Single<MyBookingResponse>

    @GET("/api/room-reservations")
    fun getBookingHistory(): Single<MyBookingResponse>

    @PATCH("/api/reservations/{id}/status")
    fun changeReservationStatus(@Path("id") reservationId: Int): Single<ChangeReservationStatusResponse>

    @PATCH("/api/room-reservations/{id}/status")
    fun changeRoomReservationStatus(
        @Path("id") roomReservationId: Int,
        @Query("status") status: BookingStatus
    ): Single<ChangeRoomReservationStatusResponse>
}
