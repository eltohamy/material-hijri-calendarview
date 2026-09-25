package com.github.eltohamy.materialhijricalendarview

import android.os.Parcelable
import com.github.eltohamy.materialhijricalendarview.CalendarUtils.day
import com.github.eltohamy.materialhijricalendarview.CalendarUtils.month
import com.github.eltohamy.materialhijricalendarview.CalendarUtils.year
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * An immutable representation of a single Hijri day (year/month/day), independent of time-of-day.
 *
 * [month] follows [java.util.Calendar] conventions (zero-based, i.e. Muharram == 0).
 */
@Parcelize
data class CalendarDay(
    val year: Int,
    val month: Int,
    val day: Int,
) : Parcelable, Comparable<CalendarDay> {

    /** This day expressed as a [Date]. */
    val date: Date get() = calendar.time

    /** A new [UmmalquraCalendar] set to this day (time-of-day cleared). */
    val calendar: UmmalquraCalendar
        get() = UmmalquraCalendar().also { copyTo(it) }

    /** Copies this day's date fields onto [calendar], clearing time-of-day. */
    fun copyTo(calendar: UmmalquraCalendar) {
        calendar.clear()
        calendar.set(year, month, day)
    }

    /** True if this day falls on the first day of its month. */
    val isFirstOfMonth: Boolean get() = day == 1

    /** The first day of this day's month. */
    val monthDay: CalendarDay get() = CalendarDay(year, month, 1)

    /** True if this day is within [min]..[max], inclusive. Either bound may be null (unbounded). */
    fun isInRange(min: CalendarDay?, max: CalendarDay?): Boolean {
        if (min != null && min > this) return false
        if (max != null && max < this) return false
        return true
    }

    override fun compareTo(other: CalendarDay): Int {
        if (year != other.year) return year - other.year
        if (month != other.month) return month - other.month
        return day - other.day
    }

    /** Number of whole calendar months between this day's month and [other]'s month (this - other). */
    fun monthsUntil(other: CalendarDay): Int =
        (other.year - year) * 12 + (other.month - month)

    /** This day's month, offset by [months] (may roll over years). */
    fun plusMonths(months: Int): CalendarDay {
        val total = year * 12 + month + months
        val newYear = Math.floorDiv(total, 12)
        val newMonth = Math.floorMod(total, 12)
        return CalendarDay(newYear, newMonth, 1)
    }

    override fun toString(): String = "CalendarDay{$year-$month-$day}"

    companion object {
        /** Today, in the Hijri calendar. */
        fun today(): CalendarDay = from(CalendarUtils.todayCalendar())

        fun from(year: Int, month: Int, day: Int): CalendarDay = CalendarDay(year, month, day)

        fun from(calendar: UmmalquraCalendar?): CalendarDay {
            requireNotNull(calendar) { "calendar cannot be null" }
            return CalendarDay(calendar.year, calendar.month, calendar.day)
        }

        fun from(date: Date?): CalendarDay? {
            date ?: return null
            val calendar = UmmalquraCalendar()
            calendar.time = date
            return from(calendar)
        }
    }
}
