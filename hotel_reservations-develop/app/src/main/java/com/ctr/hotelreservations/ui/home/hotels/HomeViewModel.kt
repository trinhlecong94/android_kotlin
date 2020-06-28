package com.ctr.hotelreservations.ui.home.hotels

import com.ctr.hotelreservations.base.BaseViewModel
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.datasource.LocalDataSource
import com.ctr.hotelreservations.data.source.response.HotelResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class HomeViewModel(
    private val localRepository: LocalDataSource,
    private val hotelRepository: HotelRepository
) : HomeVMContract, BaseViewModel() {

    private val hotels = mutableListOf<HotelResponse.Hotel>()

    override fun getHotelList(): MutableList<HotelResponse.Hotel> = hotels

    override fun getHotels(): Single<HotelResponse> {
        return hotelRepository.getHotels()
            .addProgressLoading()
            .doOnSuccess { response ->
                getHotelList().apply {
                    clear()
                    addAll(response.hotels.reversed())
                }
            }
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
