package com.ctr.hotelreservations.data.source.remote.network.adapter

import android.util.Log
import com.ctr.hotelreservations.data.source.response.ApiErrorMessage
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface CustomCallback<T> {
    /** Called for [200] responses.  */
    fun success(call: Call<T>, response: Response<T>)

    /** Called for error.  */
    fun error(throwable: Throwable)
}

/**
 * CustomCall
 */
interface CustomCall<T> {
    /**
     * Cancel call
     */
    fun cancel()

    /**
     * Enqueue call
     */
    fun enqueue(callback: CustomCallback<T>)

    /**
     * Execute call
     */
    fun execute(): Response<T>

    /**
     * Clone
     */
    fun clone(): CustomCall<T>

    /**
     * Request call
     */
    fun request(): Request

    /**
     * Check Call is canceled
     */
    fun isCanceled(): Boolean

    /**
     * Check Call is executed
     */
    fun isExecuted(): Boolean
}

internal class CustomCallAdapter<T>(private val call: Call<T>, private val retrofit: Retrofit) :
    CustomCall<T> {
    override fun execute() = call.execute()

    override fun clone() = this

    override fun request() = Request.Builder().build()

    override fun isCanceled() = call.isCanceled

    override fun isExecuted() = call.isExecuted

    override fun cancel() {
        call.cancel()
    }

    override fun enqueue(callback: CustomCallback<T>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val code = response.code()
                if (code == HttpURLConnection.HTTP_OK) {
                    callback.success(call, response)
                } else {
                    callback.error(
                        ApiErrorMessage().apply {
                            response.body().apply { Log.d("--=", "+${this}") }
                            httpStatusCode = code
                        })
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (t is IOException) {
                    if (t is UnknownHostException || t is SocketTimeoutException || t is ConnectException) {
                        val apiException = ApiErrorMessage().apply {
                            httpStatusCode = ApiErrorMessage.NETWORK_ERROR_CODE
                        }
                        callback.error(apiException)
                    } else {
                        callback.error(t)
                    }
                } else {
                    callback.error(t)
                }
            }
        })
    }
}
