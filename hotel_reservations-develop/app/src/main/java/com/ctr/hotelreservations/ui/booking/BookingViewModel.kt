package com.ctr.hotelreservations.ui.booking

import com.ctr.hotelreservations.base.BaseViewModel
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.LocalRepository
import com.ctr.hotelreservations.data.source.UserRepository
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

class BookingViewModel(
    private var localRepository: LocalRepository,
    private val hotelRepository: HotelRepository,
    private val userRepository: UserRepository
) : BookingVMContract, BaseViewModel() {

    private val roomsReservationBody = RoomsReservationBody()

    private val roomReservations = mutableListOf<MyBookingResponse.MyBooking>()

    override fun getRoomsReservationBody() = roomsReservationBody

    override fun getRoomReservations(): MutableList<MyBookingResponse.MyBooking> =
        roomReservations

    override fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>?
    ): Single<MyBookingResponse> {
        return hotelRepository.addNewRoomsReservation(
            numberOfRooms,
            listPromoCode,
            getRoomsReservationBody()
        )
            .addProgressLoading()
            .doOnSuccess { response ->
                getRoomReservations().apply {
                    clear()
                    addAll(response.myBookings)
                }
            }
    }

    override fun getUserId() = localRepository.getUserId()

    override fun getUserInfo(): Single<UserResponse> {
        return userRepository.getUserFollowId(localRepository.getUserId())
            .addProgressLoading()
    }

    override fun changeReservationStatus(reservationId: Int): Single<ChangeReservationStatusResponse> {
        return hotelRepository.changeReservationStatus(reservationId)
            .addProgressLoading()
    }

    override fun changeRoomReservationStatus(roomReservationId: Int): Single<ChangeRoomReservationStatusResponse> {
        return hotelRepository.changeRoomReservationStatus(roomReservationId)
            .addProgressLoading()
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
