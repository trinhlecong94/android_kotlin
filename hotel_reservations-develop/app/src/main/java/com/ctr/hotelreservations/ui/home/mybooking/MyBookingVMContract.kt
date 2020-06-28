package com.ctr.hotelreservations.ui.home.mybooking

import com.ctr.hotelreservations.data.source.response.MyBookingResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface MyBookingVMContract {
    fun getBookings(): MutableList<MyBookingResponse.MyBooking>

    fun getBookingHistory(): Single<MyBookingResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>
}
