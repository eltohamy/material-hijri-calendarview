package com.github.eltohamy.materialhijricalendarview

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.util.Calendar
import java.util.Date

/**
 * Small helpers around [UmmalquraCalendar] / [Calendar] used throughout the library.
 */
object CalendarUtils {

    /** A new [UmmalquraCalendar] instance set to today, with time-of-day cleared. */
    fun todayCalendar(): UmmalquraCalendar {
        val calendar = UmmalquraCalendar()
        calendar.time = Calendar.getInstance().time
        copyDateTo(calendar, calendar)
        return calendar
    }

    /** A new [Calendar] instance set to [date] (or today, if null), with time-of-day cleared. */
    fun instance(date: Date? = null): Calendar {
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        copyDateTo(calendar, calendar)
        return calendar
    }

    /** Copies only the date fields (year/month/day) from [from] into [to], clearing everything else. */
    fun copyDateTo(from: Calendar, to: Calendar) {
        val year = from.year
        val month = from.month
        val day = from.day
        to.clear()
        to.set(year, month, day)
    }

    val Calendar.year: Int get() = get(Calendar.YEAR)
    val Calendar.month: Int get() = get(Calendar.MONTH)
    val Calendar.day: Int get() = get(Calendar.DATE)
    val Calendar.dayOfWeek: Int get() = get(Calendar.DAY_OF_WEEK)
}
