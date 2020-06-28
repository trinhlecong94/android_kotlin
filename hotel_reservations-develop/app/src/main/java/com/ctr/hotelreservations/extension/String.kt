package com.ctr.hotelreservations.extension

import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
internal const val EMAIL_REGEX =
    "(?=[^@]*[A-Za-z])([a-zA-Z0-9._%+\\-])*@([a-zA-Z0-9_%+\\-.])+(\\.)([a-zA-Z]+)+"
internal const val AT_LEAST_ONE_CHAR = ".*[a-zA-Z]+.*"
internal const val AT_LEAST_LOWER_CASE = ".*[a-z]+.*"
internal const val AT_LEAST_UPPER_CASE = ".*[A-Z]+.*"
internal const val AT_LEAST_ONE_NUMBER = ".*[0-9]+.*"
private const val PASSWORD_REGEX = "^[A-Za-z0-9]+\$"

fun String.isEmailValid(): Boolean {
    if (!Pattern.compile(EMAIL_REGEX).matcher(this).matches()) return false

    val arr = this.split("@")
    if (arr.count() != 2) {
        return false
    }
    try {
        val subDomain = arr[1].subSequence(0, arr[1].lastIndexOf("."))
        if (subDomain.last() == '.') return false
        return Pattern.compile(AT_LEAST_ONE_CHAR).matcher(subDomain).matches()
    } catch (e: IndexOutOfBoundsException) {
        e.printStackTrace()
    }
    return true
}

fun String.isPasswordValid(): Boolean {
    if (this.length < 8 || this.length > 100) return false
    return Pattern.compile(AT_LEAST_ONE_NUMBER).matcher(this).matches()
            && Pattern.compile(AT_LEAST_LOWER_CASE).matcher(this).matches()
            && Pattern.compile(AT_LEAST_UPPER_CASE).matcher(this).matches()
            && Pattern.compile(PASSWORD_REGEX).matcher(this).matches()
}

internal fun String.getPriceFormat(): String =
    "$" + NumberFormat.getNumberInstance(Locale.US).format(this.toDouble())
