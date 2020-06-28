package com.ctr.hotelreservations.data.source.response

import com.google.gson.annotations.SerializedName


/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
data class ApiErrorMessage(
    @SerializedName("debugMessage") val debugMessage: String? = null,
    @SerializedName("message") val messageX: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("subErrors") val subErrors: List<SubError>? = null,
    @SerializedName("timestamp") val timestamp: String? = null
) : Throwable(messageX) {
    companion object {
        internal const val NETWORK_ERROR_CODE = 700
    }

    var httpStatusCode: Int? = null
}

data class SubError(
    @SerializedName("field") val fieldX: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("object") val objectX: String? = null,
    @SerializedName("rejectedValue") val rejectedValue: String? = null
)

