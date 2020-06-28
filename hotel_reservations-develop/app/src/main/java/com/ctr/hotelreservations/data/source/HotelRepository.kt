package com.ctr.hotelreservations.data.source

import com.ctr.hotelreservations.data.source.datasource.HotelDataSource
import com.ctr.hotelreservations.data.source.remote.HotelRemoteDataSource
import com.ctr.hotelreservations.data.source.request.RoomsReservationBody

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class HotelRepository : HotelDataSource {

    private val hotelRemoteDataSource = HotelRemoteDataSource()

    override fun getHotels() = hotelRemoteDataSource.getHotels()

    override fun getAllRoomByBrand(brandId: Int) = hotelRemoteDataSource.getAllRoomByBrand(brandId)

    override fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String) =
        hotelRemoteDataSource.getAllRoomStatus(brandId, startDate, endDate)

    override fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>?,
        roomsReservationBody: RoomsReservationBody
    ) = hotelRemoteDataSource.addNewRoomsReservation(
        numberOfRooms,
        listPromoCode,
        roomsReservationBody
    )

    override fun getAllPromoStillActive() = hotelRemoteDataSource.getAllPromoStillActive()

    override fun getBookingHistory() = hotelRemoteDataSource.getBookingHistory()

    override fun changeReservationStatus(reservationId: Int) =
        hotelRemoteDataSource.changeReservationStatus(reservationId)

    override fun changeRoomReservationStatus(roomReservationId: Int) =
        hotelRemoteDataSource.changeRoomReservationStatus(roomReservationId)
}
