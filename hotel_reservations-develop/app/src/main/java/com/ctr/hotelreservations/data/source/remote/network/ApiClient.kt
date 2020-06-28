package com.ctr.hotelreservations.data.source.remote.network

import com.ctr.hotelreservations.BuildConfig
import com.ctr.hotelreservations.data.source.remote.network.adapter.CustomCallAdapterFactory
import com.ctr.hotelreservations.extension.getScreenHeight
import com.ctr.hotelreservations.extension.getScreenWidth
import com.ctr.hotelreservations.ui.App
import com.ctr.hotelreservations.util.Header
import com.ctr.hotelreservations.util.SharedReferencesUtil
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient private constructor(url: String? = null) {

    private var baseUrl: String =
        if (url == null || url.isEmpty()) BuildConfig.BASE_API_URL else url

    companion object : SingletonHolder<ApiClient, String>(::ApiClient) {
        private const val API_TIMEOUT = 170L // 170 seconds
        private const val LOW_INTERNET_THRESHOLD_NORMAL = 5
        private const val LOW_INTERNET_THRESHOLD_SPECIAL = 100
        private const val CONTENT_TYPE = "Content-Type"
        private const val X_DEVICE_FINGERPRINTING = "x-device-fingerprinting"
        private const val USER_AGENT = "User-Agent"
        private const val DEVICE_TYPE = "Device-Type"
        private const val IS_ANDROID = "Content-Type"
        private const val AUTHORIZATION = "Authorization"
    }

    val service: ApiService
        get() {
            return createService()
        }

    private fun createService(): ApiService {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.interceptors().add(Interceptor { chain ->
            // Request customization: add request headers
            val original = chain.request()
            //TODO: It will confirm maybe sc or tz will change later
            val valueOfDeviceFingerprinting =
                "sc=24;sh=${App.instance.applicationContext.getScreenHeight()};sw=${App.instance.applicationContext.getScreenWidth()};tz=-540"
            val userAgent =
                "${BuildConfig.APP_NAME}/${BuildConfig.VERSION_NAME} (${BuildConfig.APPLICATION_ID}; build:${BuildConfig.VERSION_CODE}; ANDROID ${android.os.Build.VERSION.RELEASE})"

            val requestBuilder = original.newBuilder()
                .method(original.method(), original.body())
                .addHeader(CONTENT_TYPE, "application/json")
//                .addHeader(X_DEVICE_FINGERPRINTING, valueOfDeviceFingerprinting)
//                .addHeader(USER_AGENT, userAgent)
//                .addHeader(DEVICE_TYPE, "SmartDevice")
//                .addHeader(IS_ANDROID, "true")
//                .addHeader(Header.X_XSRF_TOKEN, Header.getXSRFToken())
                .addHeader(AUTHORIZATION, App.instance.localRepository.getAutoLoginToken() ?: "")

            val cookie = Header.getCookie()
            if (cookie.isNotEmpty()) {
                requestBuilder.addHeader(Header.COOKIE, cookie)
            }
            val threshold =
                if (original.url().toString().contains("/Sync/Update")) {
                    LOW_INTERNET_THRESHOLD_SPECIAL
                } else {
                    LOW_INTERNET_THRESHOLD_NORMAL
                }
            val lowInternetThread = LowInternetThread(threshold)
            lowInternetThread.start()
            val request = requestBuilder.build()
            val response = chain.proceed(request)
            lowInternetThread.cancel()
            if (request.method() == "POST") {
                SharedReferencesUtil.setString(
                    App.instance.applicationContext,
                    Header.X_XSRF_TOKEN,
                    response.headers()[Header.X_XSRF_TOKEN] ?: ""
                )
            }

            response.headers().get("Set-Cookie")?.let {
                val currentCookie = it.split(";")[0]
                if (currentCookie.contains(Header.COOKIE_JSESSION_ID)) {
                    SharedReferencesUtil.setString(
                        App.instance.applicationContext,
                        Header.COOKIE_JSESSION_ID,
                        currentCookie
                    )
                } else if (currentCookie.contains(Header.COOKIE_AWSALB)) {
                    SharedReferencesUtil.setString(
                        App.instance.applicationContext,
                        Header.COOKIE_AWSALB,
                        currentCookie
                    )
                }
            }
            try {
                val headerString = response.headers().toString()
                if (headerString.contains("set-cookie: df=")) {
                    val cookieDf =
                        "df=" + headerString.split("set-cookie: df=")[1].split(";")[0].trim()
                    SharedReferencesUtil.setString(
                        App.instance.applicationContext,
                        Header.COOKIE,
                        cookieDf
                    )
                }
            } catch (e: Exception) {
            }
            response
        })
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        val client = httpClientBuilder
            .connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val gs = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gs))
            .addCallAdapterFactory(CustomCallAdapterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}

/**
 * Use this class to create singleton object with argument
 */
open class SingletonHolder<out T, in A>(private var creator: (A?) -> T) {
    @kotlin.jvm.Volatile
    private var instance: T? = null

    /**
     * Generate instance for T class with argument A
     */
    fun getInstance(arg: A?): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator(arg)
                instance = created
                created
            }
        }
    }

    /**
     * Clear current instance
     */
    fun clearInstance() {
        instance = null
    }
}
