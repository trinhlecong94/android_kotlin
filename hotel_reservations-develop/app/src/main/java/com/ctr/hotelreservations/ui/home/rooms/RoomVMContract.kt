package com.ctr.hotelreservations.ui.home.rooms

import com.ctr.hotelreservations.data.source.response.RoomResponse
import com.ctr.hotelreservations.data.source.response.RoomTypeResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface RoomVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getRooms(): MutableList<RoomResponse.Room>

    fun getRoomTypes(): MutableList<RoomTypeResponse.RoomTypeStatus>

    fun getAllRoomByBrand(brandId: Int): Single<RoomResponse>

    fun getAllRoomStatus(
        brandId: Int,
        startDate: String,
        endDate: String,
        numOfGuest: Int,
        numOfRoom: Int
    ): Single<RoomTypeResponse>

    fun filterRoomStatus(numOfGuest: Int, numOfRoom: Int)
}
