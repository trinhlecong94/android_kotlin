package com.ctr.hotelreservations.util

import com.ctr.hotelreservations.ui.App


object Header {
    internal const val COOKIE = "Cookie"
    internal const val X_XSRF_TOKEN = "x-xsrf-token"
    internal const val COOKIE_JSESSION_ID = "JSESSIONID"
    internal const val COOKIE_AWSALB = "AWSALB"

    internal fun getCookie(): String {
        var cookie = ""
        if (SharedReferencesUtil.getString(
                App.instance.applicationContext,
                COOKIE
            )?.isNotEmpty() == true
        ) {
            SharedReferencesUtil.getString(App.instance.applicationContext, COOKIE)
                ?.let { cookie = cookie.plus(it) }
        }

        if (SharedReferencesUtil.getString(
                App.instance.applicationContext,
                COOKIE_AWSALB
            )?.isNotEmpty() == true
        ) {
            if (cookie.isNotEmpty()) {
                cookie = cookie.plus(";")
            }
            SharedReferencesUtil.getString(
                App.instance.applicationContext,
                COOKIE_AWSALB
            )
                ?.let { cookie.plus(it) }
        }

        if (SharedReferencesUtil.getString(
                App.instance.applicationContext,
                COOKIE_JSESSION_ID
            )?.isNotEmpty() == true
        ) {
            if (cookie.isNotEmpty()) {
                cookie = cookie.plus(";")
            }
            SharedReferencesUtil.getString(
                App.instance.applicationContext,
                COOKIE_JSESSION_ID
            )
                ?.let { cookie = cookie.plus(it) }
        }
        return cookie
    }

    internal fun getXSRFToken(): String =
        SharedReferencesUtil.getString(App.instance.applicationContext, X_XSRF_TOKEN) ?: ""
}
