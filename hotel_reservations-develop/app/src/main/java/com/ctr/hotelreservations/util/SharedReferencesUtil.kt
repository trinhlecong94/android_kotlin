package com.ctr.hotelreservations.util

import android.content.Context
import com.ctr.hotelreservations.BuildConfig

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
object SharedReferencesUtil {

    private const val SHARED_PREFERENCES_NAME = BuildConfig.APPLICATION_ID
    internal const val KEY_IS_FIRST_LAUNCH = "key_is_first"
    internal const val KEY_DEVICE_TOKEN = "key_device_token"
    internal const val KEY_STATUS_BAR_HEIGHT = "key_status_bar_height"
    internal const val KEY_AUTO_LOGIN_TOKEN = "key_auto_login_token"
    internal const val KEY_USER_ID = "key_user_id"

    internal fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        return context
            .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getBoolean(key, defaultValue)

    }

    internal fun setBoolean(context: Context, key: String, value: Boolean): Boolean {
        return context
            .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(key, value)
            .commit()
    }

    internal fun getString(context: Context, key: String): String? {
        return context
            .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getString(key, null)
    }

    internal fun setString(context: Context, key: String, value: String) {
        context
            .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit().putString(key, value)
            .apply()
    }

    internal fun remove(context: Context, key: String) =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().remove(
            key
        ).apply()

    internal fun getInt(context: Context, key: String, default: Int) = context.getSharedPreferences(
        SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
    ).getInt(key, default)

    internal fun setInt(context: Context, key: String, value: Int) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit().putInt(key, value)
            .apply()
    }
}
