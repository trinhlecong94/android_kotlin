package com.ctr.hotelreservations.data.source.datasource

import com.ctr.hotelreservations.data.source.request.RoomsReservationBody
import com.ctr.hotelreservations.data.source.response.*
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
interface HotelDataSource {
    fun getHotels(): Single<HotelResponse>

    fun getAllRoomByBrand(brandId: Int): Single<RoomResponse>

    fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String): Single<RoomTypeResponse>

    fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>?,
        roomsReservationBody: RoomsReservationBody
    ): Single<MyBookingResponse>

    fun getAllPromoStillActive(): Single<PromoResponse>

    fun getBookingHistory(): Single<MyBookingResponse>

    fun changeReservationStatus(reservationId: Int): Single<ChangeReservationStatusResponse>

    fun changeRoomReservationStatus(roomReservationId: Int): Single<ChangeRoomReservationStatusResponse>
}
