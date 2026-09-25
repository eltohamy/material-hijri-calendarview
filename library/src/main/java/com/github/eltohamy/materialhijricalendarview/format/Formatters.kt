package com.github.eltohamy.materialhijricalendarview.format

import com.github.eltohamy.materialhijricalendarview.CalendarDay
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Supplies the label for a single day cell. Default: day-of-month, e.g. "7". */
fun interface DayFormatter {
    fun format(day: CalendarDay): String

    companion object {
        val Default: DayFormatter = DateFormatDayFormatter()
    }
}

/** Supplies the label for a weekday header cell. */
fun interface WeekDayFormatter {
    /** @param dayOfWeek a [java.util.Calendar] `DAY_OF_WEEK` constant (`Calendar.SUNDAY`..`Calendar.SATURDAY`) */
    fun format(dayOfWeek: Int): CharSequence

    companion object {
        val Default: WeekDayFormatter = CalendarWeekDayFormatter()
    }
}

/** Supplies the month/year title shown above the calendar. */
fun interface TitleFormatter {
    fun format(month: CalendarDay): CharSequence

    companion object {
        val Default: TitleFormatter = DateFormatTitleFormatter()
    }
}

/** Formats a day using a [DateFormat], defaulting to "d" (day of month) in the current locale. */
class DateFormatDayFormatter(
    private val dateFormat: DateFormat = SimpleDateFormat("d", Locale.getDefault()),
) : DayFormatter {
    override fun format(day: CalendarDay): String {
        dateFormat.calendar = day.calendar
        return dateFormat.format(day.date)
    }
}

/** Formats the month/year title as "<short month name> <year>", e.g. "Ramadan 1447". */
class DateFormatTitleFormatter: TitleFormatter {
    override fun format(month: CalendarDay): CharSequence {
        val calendar = month.calendar
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        return "$monthName ${month.year}"
    }
}

/** Formats weekday labels using [Calendar.getDisplayName] for the current locale. */
class CalendarWeekDayFormatter(
    private val style: Int = Calendar.SHORT,
    private val locale: Locale = Locale.getDefault(),
) : WeekDayFormatter {
    private val calendar = Calendar.getInstance()
    override fun format(dayOfWeek: Int): CharSequence {
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, style, locale) ?: ""
    }
}

/** Formats weekday labels from a fixed 7-element array, starting with Sunday. */
class ArrayWeekDayFormatter(private val weekDayLabels: List<CharSequence>) : WeekDayFormatter {
    init {
        require(weekDayLabels.size == 7) { "weekDayLabels must contain exactly 7 elements" }
    }

    override fun format(dayOfWeek: Int): CharSequence = weekDayLabels[dayOfWeek - 1]
}

/** Formats the month/year title from a fixed 12-element month-name array, starting with Muharram. */
class MonthArrayTitleFormatter(private val monthLabels: List<CharSequence>) : TitleFormatter {
    init {
        require(monthLabels.size >= 12) { "monthLabels must contain at least 12 elements" }
    }

    override fun format(month: CalendarDay): CharSequence = "${monthLabels[month.month]} ${month.year}"
}
