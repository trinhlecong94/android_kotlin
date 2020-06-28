package com.ctr.hotelreservations.data.model

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
data class RoomInfo(
    val roomId: Int,
    val category: String,
    val location: String,
    val description: String,
    val isLike: Boolean,
    val title: String,
    val price: Long,
    val discount: Float,
    val ratingStar: Int
)