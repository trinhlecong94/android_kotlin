package com.ctr.hotelreservations.ui.roomdetail

import com.ctr.hotelreservations.data.source.response.PromoResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*

interface RoomDetailVMContract {

    fun getPromos(): MutableList<PromoResponse.Promo>

    fun getPromoByRoomType(roomTypeId: Int, startDate: Calendar): Single<PromoResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>
    fun getMaxPromo(): PromoResponse.Promo?
}
