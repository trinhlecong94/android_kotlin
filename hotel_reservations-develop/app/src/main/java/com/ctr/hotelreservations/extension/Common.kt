package com.ctr.hotelreservations.extension

import com.google.gson.Gson

/**
 * Created by at-trinhnguyen2 on
 */
internal fun Any.toJsonString() = Gson().toJson(this)
