package com.github.eltohamy.materialhijricalendarview

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.Calendar

/** How dates can be selected in a [HijriCalendarView]. */
enum class HijriSelectionMode {
    /** Tapping a day does nothing. */
    NONE,

    /** Tapping a day selects it, replacing any previous selection. */
    SINGLE,

    /** Tapping a day toggles it in or out of the selection, independent of other selected days. */
    MULTIPLE,

    /** The first tap starts a range, the second tap closes it; a further tap starts a new range. */
    RANGE,
}

/**
 * State for [HijriCalendarView]: which month page is showing, the min/max selectable range,
 * first day of the week, and the current selection. Create with [rememberHijriCalendarState].
 */
@Stable
class HijriCalendarState internal constructor(
    val pagerState: PagerState,
    internal val bounds: Bounds,
    firstDayOfWeek: Int,
    selectionMode: HijriSelectionMode,
    selectedDates: Set<CalendarDay>,
    rangeSelection: ClosedRange<CalendarDay>?,
) {
    /** The earliest selectable day, or null if unbounded. */
    val minDate: CalendarDay? get() = bounds.minDate

    /** The latest selectable day, or null if unbounded. */
    val maxDate: CalendarDay? get() = bounds.maxDate

    var firstDayOfWeek by mutableIntStateOf(firstDayOfWeek)

    var selectionMode: HijriSelectionMode by mutableStateOf(selectionMode)

    /** Selected days when [selectionMode] is [HijriSelectionMode.SINGLE] or [HijriSelectionMode.MULTIPLE]. */
    var selectedDates: Set<CalendarDay> by mutableStateOf(selectedDates)
        internal set

    /** The selected range when [selectionMode] is [HijriSelectionMode.RANGE], if any. */
    var rangeSelection: ClosedRange<CalendarDay>? by mutableStateOf(rangeSelection)
        internal set

    /** The first day of the month currently centered in the pager. */
    val currentMonth: CalendarDay get() = bounds.monthAt(pagerState.currentPage)

    fun monthAt(page: Int): CalendarDay = bounds.monthAt(page)

    fun indexOf(month: CalendarDay): Int = bounds.indexOf(month)

    /** True if [day] is part of the current selection, under any selection mode. */
    fun isSelected(day: CalendarDay): Boolean = when (selectionMode) {
        HijriSelectionMode.RANGE -> rangeSelection?.contains(day) == true
        else -> day in selectedDates
    }

    /** Clears the selection, regardless of [selectionMode]. */
    fun clearSelection() {
        selectedDates = emptySet()
        rangeSelection = null
    }

    /** Applies a tap on [day], following the current [selectionMode]'s rules. */
    fun toggleDate(day: CalendarDay) {
        if (day.isInRange(minDate, maxDate).not()) return
        when (selectionMode) {
            HijriSelectionMode.NONE -> Unit
            HijriSelectionMode.SINGLE -> selectedDates = setOf(day)
            HijriSelectionMode.MULTIPLE -> selectedDates =
                if (day in selectedDates) selectedDates - day else selectedDates + day
            HijriSelectionMode.RANGE -> {
                val current = rangeSelection
                rangeSelection = when {
                    current == null || current.start != current.endInclusive -> day..day
                    day < current.start -> day..current.start
                    else -> current.start..day
                }
            }
        }
    }

    /** Animates the pager to [month]. */
    suspend fun animateToMonth(month: CalendarDay) {
        pagerState.animateScrollToPage(indexOf(month))
    }

    /** Jumps the pager to [month] without animation. */
    suspend fun scrollToMonth(month: CalendarDay) {
        pagerState.scrollToPage(indexOf(month))
    }

    /** True if there is a later month within range to page to. */
    val canGoForward: Boolean get() = pagerState.currentPage < bounds.pageCount - 1

    /** True if there is an earlier month within range to page to. */
    val canGoBack: Boolean get() = pagerState.currentPage > 0

    /**
     * Maps month <-> pager page index for an (optionally unbounded) min/max date range.
     * When unbounded, defaults to +/-200 years around today, matching the original library.
     */
    internal class Bounds(val minDate: CalendarDay?, val maxDate: CalendarDay?) {
        private val rangeStart: CalendarDay =
            (minDate ?: CalendarDay.today().plusMonths(-DEFAULT_RANGE_MONTHS)).monthDay
        private val rangeEnd: CalendarDay =
            (maxDate ?: CalendarDay.today().plusMonths(DEFAULT_RANGE_MONTHS)).monthDay
        val pageCount: Int = (rangeStart.monthsUntil(rangeEnd) + 1).coerceAtLeast(1)

        fun indexOf(month: CalendarDay): Int =
            rangeStart.monthsUntil(month.monthDay).coerceIn(0, pageCount - 1)

        fun monthAt(page: Int): CalendarDay =
            rangeStart.plusMonths(page.coerceIn(0, pageCount - 1))

        private companion object {
            const val DEFAULT_RANGE_MONTHS = 200 * 12
        }
    }
}

/**
 * Creates and remembers a [HijriCalendarState].
 *
 * @param initialMonth month shown initially (only its year/month are used)
 * @param minDate earliest selectable day, or null for unbounded
 * @param maxDate latest selectable day, or null for unbounded
 * @param firstDayOfWeek a [Calendar] `DAY_OF_WEEK` constant; defaults to the current locale's convention
 * @param selectionMode how days can be selected
 * @param selectedDates initially selected days, for [HijriSelectionMode.SINGLE]/[HijriSelectionMode.MULTIPLE]
 * @param rangeSelection initially selected range, for [HijriSelectionMode.RANGE]
 */
@Composable
fun rememberHijriCalendarState(
    initialMonth: CalendarDay = CalendarDay.today(),
    minDate: CalendarDay? = null,
    maxDate: CalendarDay? = null,
    firstDayOfWeek: Int = remember { Calendar.getInstance().firstDayOfWeek },
    selectionMode: HijriSelectionMode = HijriSelectionMode.SINGLE,
    selectedDates: Set<CalendarDay> = emptySet(),
    rangeSelection: ClosedRange<CalendarDay>? = null,
): HijriCalendarState {
    val bounds = remember(minDate, maxDate) { HijriCalendarState.Bounds(minDate, maxDate) }
    val pagerState = rememberPagerState(
        initialPage = remember(bounds, initialMonth) { bounds.indexOf(initialMonth) },
        pageCount = { bounds.pageCount },
    )
    return remember(pagerState, bounds) {
        HijriCalendarState(
            pagerState = pagerState,
            bounds = bounds,
            firstDayOfWeek = firstDayOfWeek,
            selectionMode = selectionMode,
            selectedDates = selectedDates,
            rangeSelection = rangeSelection,
        )
    }
}
