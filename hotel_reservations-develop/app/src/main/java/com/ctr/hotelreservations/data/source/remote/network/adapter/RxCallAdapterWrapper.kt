package com.ctr.hotelreservations.data.source.remote.network.adapter

import com.ctr.hotelreservations.data.source.response.ApiErrorMessage
import com.ctr.hotelreservations.data.source.response.SubError
import okhttp3.ResponseBody
import retrofit2.*
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

class RxCallAdapterWrapper<R>(type: Type, retrofit: Retrofit, wrapped: CallAdapter<R, *>?) :
    BaseRxCallAdapterWrapper<R>(type, retrofit, wrapped) {

    /*override fun convertRetrofitExceptionToCustomException(
        throwable: Throwable,
        retrofit: Retrofit
    ): Throwable {
        if (throwable is HttpException) {
            return ApiErrorMessage().apply {
                Log.d("--=", "convertRetrofitExceptionToCustomException: ${throwable.response()}")
                httpStatusCode = throwable.code()
            }
        }

        // Set message error by this case of status code
        if (throwable is UnknownHostException || throwable is ConnectException || throwable is SocketTimeoutException) {
            return ApiErrorMessage().apply {
                httpStatusCode = ApiErrorMessage.NETWORK_ERROR_CODE
            }
        }
        return throwable
    }*/

    override fun convertRetrofitExceptionToCustomException(
        throwable: Throwable,
        retrofit: Retrofit
    ): Throwable {

        if (throwable is HttpException) {
            val converter: Converter<ResponseBody, ApiErrorMessage> =
                retrofit.responseBodyConverter(
                    ApiErrorMessage::class.java,
                    arrayOfNulls<Annotation>(0)
                )
            val response: Response<*>? = throwable.response()
            when (response?.code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> response.errorBody()?.let {
                    val apiException = converter.convert(it)!!
                    apiException.httpStatusCode = HttpsURLConnection.HTTP_UNAUTHORIZED
                    return apiException
                }

                HttpsURLConnection.HTTP_BAD_REQUEST -> response.errorBody()?.let {
                    return converter.convert(it)!!.apply {
                        httpStatusCode = HttpsURLConnection.HTTP_BAD_REQUEST
                    }
                }

                HttpsURLConnection.HTTP_INTERNAL_ERROR -> response.errorBody()?.let {
                    return converter.convert(it)!!
                }

                HttpsURLConnection.HTTP_FORBIDDEN -> response.errorBody()?.let {
                    val apiException = converter.convert(it)
                    apiException!!.httpStatusCode = HttpURLConnection.HTTP_FORBIDDEN
                    return apiException
                }

                HttpsURLConnection.HTTP_NOT_FOUND -> response.errorBody()?.let {
                    return converter.convert(it)!!
                }

                HttpsURLConnection.HTTP_NOT_ACCEPTABLE -> response.errorBody()?.let {
                    return converter.convert(it)!!
                }

                HttpsURLConnection.HTTP_CONFLICT -> response.errorBody()?.let {
                    return converter.convert(it)!!.apply {
                        httpStatusCode = HttpsURLConnection.HTTP_CONFLICT
                    }
                }
            }
        }

        if (throwable is UnknownHostException) {
            // Set message error of this case in activity extension
            val apiException = ApiErrorMessage("", "", "", listOf(SubError("", "", "", "")), "")
            apiException.httpStatusCode = ApiErrorMessage.NETWORK_ERROR_CODE
            return apiException
        }

        if (throwable is SocketTimeoutException) {
            val apiException = ApiErrorMessage("", "", "", listOf(SubError("", "", "", "")), "")
            apiException.httpStatusCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT
            return apiException
        }

        return throwable
    }
}
