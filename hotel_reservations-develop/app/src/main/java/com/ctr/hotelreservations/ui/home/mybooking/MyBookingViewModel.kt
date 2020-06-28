package com.ctr.hotelreservations.ui.home.mybooking

import com.ctr.hotelreservations.base.BaseViewModel
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.response.MyBookingResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class MyBookingViewModel(
    private val hotelRepository: HotelRepository
) : MyBookingVMContract, BaseViewModel() {

    private val bookings = mutableListOf<MyBookingResponse.MyBooking>()

    override fun getBookings(): MutableList<MyBookingResponse.MyBooking> = bookings

    override fun getBookingHistory(): Single<MyBookingResponse> {
        return hotelRepository.getBookingHistory()
            .doOnSuccess { response ->
                getBookings().apply {
                    clear()
                    addAll(response.myBookings)
                    sortByDescending { it.id }
                }
            }
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
