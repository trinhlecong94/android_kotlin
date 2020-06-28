package com.ctr.hotelreservations.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * Created by at-chauhoang on 11/06/2019.
 */
object DateUtil {
    internal const val FORMAT_DATE_TIME_FROM_API2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    internal const val FORMAT_DATE_TIME_FROM_API = "yyyy-MM-dd'T'HH:mm:ss.SSS'z'"
    internal const val FORMAT_DATE_TIME_FROM_API_3 = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    internal const val FORMAT_DATE_TIME = "yyyy/MM/dd"
    internal const val FORMAT_DATE_TIME_CHECK_IN = "dd/MM/yyyy"
    internal const val FORMAT_DATE_DAY_MONTH = "dd/MM"
    internal const val FORMAT_DATE_PROMO_SERVER = "yyyy-MM-dd"
    internal const val FORMAT_DATE_TIME_CHECK_IN_BOOKING = "dd MMM, yyyy"
    internal const val FORMAT_DATE_TIME_DAY_IN_WEEK = "EEEE"
    internal const val ONE_HOUR = 1000 * 60 * 60

    internal fun parse(targetString: String, format: String): Calendar {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        try {
            val calendar = Calendar.getInstance()
            calendar.time = formatter.parse(targetString)
            return calendar
        } catch (e: ParseException) {
            throw IllegalArgumentException("non-parsable date format. target: $targetString, format: $format")
        }
    }

    internal fun format(calendar: Calendar, format: String): String {
        val resultFormat = SimpleDateFormat(format, Locale.getDefault())
        return resultFormat.format(calendar.time)
    }

    internal fun formatToUTC(calendar: Calendar, format: String): String {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        return convertTimeLongToStringWithGMT(calendar.timeInMillis, format)
    }

    internal fun convertDateUTCToLocalDate(targetString: String, format: String): Date {
        try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            return sdf.parse(targetString)
        } catch (e: Exception) {
            throw IllegalArgumentException("non-parsable date format. target: $targetString, format: $format")
        }
    }

    internal fun convertTimeFromApi(
        time: String,
        format: String = FORMAT_DATE_TIME_FROM_API
    ): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val calendar = Calendar.getInstance()
            calendar.time = formatter.parse(time)
            formatter.timeZone = TimeZone.getTimeZone("Japan")
            return formatter.format(calendar.time)
        } catch (e: ParseException) {
            throw IllegalArgumentException("non-parsable date format. target: $time, format: $format")
        }
    }

    internal fun convertTimeToUTC(
        time: String,
        format: String = FORMAT_DATE_TIME_FROM_API
    ): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("Japan")
        try {
            val calendar = Calendar.getInstance()
            calendar.time = formatter.parse(time)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.format(calendar.time)
        } catch (e: ParseException) {
            throw IllegalArgumentException("non-parsable date format. target: $time, format: $format")
        }
    }

    private fun convertTimeLongToStringWithGMT(timeInMillis: Long, format: String): String {
        try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("GMT")
            return df.format(Date(timeInMillis)).toString()
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message)
        }
    }
}

internal fun String.parseToCalendar(): Calendar {
    return DateUtil.parse(this, DateUtil.FORMAT_DATE_TIME_FROM_API)
}

internal fun String.parseToCalendar(format: String): Calendar {
    return DateUtil.parse(this, format)
}

internal fun Calendar.parseToString(): String {
    return DateUtil.format(this, DateUtil.FORMAT_DATE_TIME_FROM_API)
}

internal fun Calendar?.compareDay(otherDate: Calendar?): Int {
    if (this == null || otherDate == null) return -1
    return ((abs(this.timeInMillis - otherDate.timeInMillis) + 2 * DateUtil.ONE_HOUR) / (24 * DateUtil.ONE_HOUR)).toInt()
}

internal fun Calendar.parseToString(format: String): String {
    return DateUtil.format(this, format)
}

internal fun calculateHighlightedDays(startDate: Calendar, endDate: Calendar): List<Calendar?> {
    var numDays = startDate.compareDay(endDate)

    // In case user chooses an end day before the start day.
    var dir = 1
    if (numDays < 0) {
        dir = -1
    }
    numDays = abs(numDays)

    // +1 to account for the end day which should be highlighted as well
    val highlightedDays = mutableListOf<Calendar>()
    for (i in 0 until numDays) {
        highlightedDays.add(
            GregorianCalendar(
                startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DAY_OF_MONTH)
            )
        )
        highlightedDays[i].add(Calendar.DAY_OF_MONTH, i * dir)
    }
    highlightedDays.add(endDate)
    return highlightedDays
}
