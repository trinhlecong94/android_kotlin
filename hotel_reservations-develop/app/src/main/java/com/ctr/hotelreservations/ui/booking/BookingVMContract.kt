package com.ctr.hotelreservations.ui.booking

import com.ctr.hotelreservations.data.source.request.RoomsReservationBody
import com.ctr.hotelreservations.data.source.response.ChangeReservationStatusResponse
import com.ctr.hotelreservations.data.source.response.ChangeRoomReservationStatusResponse
import com.ctr.hotelreservations.data.source.response.MyBookingResponse
import com.ctr.hotelreservations.data.source.response.UserResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */
interface BookingVMContract {

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getRoomReservations(): MutableList<MyBookingResponse.MyBooking>

    fun getRoomsReservationBody(): RoomsReservationBody

    fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>? = null
    ): Single<MyBookingResponse>

    fun getUserInfo(): Single<UserResponse>

    fun getUserId(): Int
    fun changeReservationStatus(reservationId: Int): Single<ChangeReservationStatusResponse>
    fun changeRoomReservationStatus(roomReservationId: Int): Single<ChangeRoomReservationStatusResponse>
}
