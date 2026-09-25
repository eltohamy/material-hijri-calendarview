@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.eltohamy.materialhijricalendarview.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.eltohamy.materialhijricalendarview.CalendarDay
import com.github.eltohamy.materialhijricalendarview.HijriCalendarView
import com.github.eltohamy.materialhijricalendarview.HijriDayDecorator
import com.github.eltohamy.materialhijricalendarview.HijriDayStyle
import com.github.eltohamy.materialhijricalendarview.HijriSelectionMode
import com.github.eltohamy.materialhijricalendarview.HijriShowOtherDates
import com.github.eltohamy.materialhijricalendarview.rememberHijriCalendarState
import java.util.Calendar

/** Single-date selection with default styling — the Compose equivalent of the old BasicActivity. */
@Composable
fun BasicScreen() {
    val state = rememberHijriCalendarState()
    Column(modifier = Modifier.padding(16.dp)) {
        HijriCalendarView(state = state, showOtherDates = HijriShowOtherDates.Defaults)
        VSpace()
        Text("Selected: ${state.selectedDates.firstOrNull() ?: "none"}")
    }
}

/**
 * Decorators as plain functions of [CalendarDay] -> [HijriDayStyle], replacing the old
 * `DayViewDecorator`/`DayViewFacade` sample (`HighlightWeekendsDecorator`, `EventDecorator`).
 */
@Composable
fun DecoratedScreen() {
    val state = rememberHijriCalendarState()

    val weekendDecorator = remember {
        HijriDayDecorator { day ->
            val dow = day.calendar.get(Calendar.DAY_OF_WEEK)
            if (dow == Calendar.FRIDAY || dow == Calendar.SATURDAY) {
                HijriDayStyle(backgroundColor = Color(0xFFE3F2FD))
            } else null
        }
    }

    // A handful of "events" on fixed days of the visible month, purely for illustration.
    val eventDays = remember(state.currentMonth) {
        setOf(
            CalendarDay(state.currentMonth.year, state.currentMonth.month, 5),
            CalendarDay(state.currentMonth.year, state.currentMonth.month, 12),
            CalendarDay(state.currentMonth.year, state.currentMonth.month, 21),
        )
    }
    val eventDecorator = remember(eventDays) {
        HijriDayDecorator { day -> if (day in eventDays) HijriDayStyle(dotColor = Color(0xFFD32F2F)) else null }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        HijriCalendarView(
            state = state,
            decorators = listOf(weekendDecorator, eventDecorator),
        )
        VSpace()
        Text("Blue = Friday/Saturday. Red dot = a sample \"event\" day.")
    }
}

/** Disabling specific days via a decorator that returns `enabled = false`. */
@Composable
fun DisableDaysScreen() {
    val state = rememberHijriCalendarState()
    val disableFridays = remember {
        HijriDayDecorator { day ->
            if (day.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                HijriDayStyle(enabled = false)
            } else null
        }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        HijriCalendarView(
            state = state,
            decorators = listOf(disableFridays),
            showOtherDates = HijriShowOtherDates.Defaults, // keeps decorated-disabled days visible, but not tappable
        )
        VSpace()
        Text("Every Friday is disabled via a decorator.")
    }
}

/** Bounding the selectable range and picking a date range (min/max date + [HijriSelectionMode.RANGE]). */
@Composable
fun RangeScreen() {
    val today = remember { CalendarDay.today() }
    val minDate = remember { today.plusMonths(-1) }
    val maxDate = remember { today.plusMonths(2) }
    val state = rememberHijriCalendarState(
        minDate = minDate,
        maxDate = maxDate,
        selectionMode = HijriSelectionMode.RANGE,
    )
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Selectable range: ${minDate.monthDay} .. ${maxDate.monthDay}")
        VSpace()
        HijriCalendarView(state = state, showOtherDates = HijriShowOtherDates.All)
        VSpace()
        val range = state.rangeSelection
        Text(
            if (range == null) "No range selected yet — tap a start day, then an end day."
            else "Selected range: ${range.start} .. ${range.endInclusive}",
        )
    }
}

/** Live-adjustable settings, replacing the old DynamicSettersActivity's imperative setter calls. */
@Composable
fun DynamicSettersScreen() {
    val state = rememberHijriCalendarState()
    var showTopBar by remember { mutableStateOf(true) }
    var pagingEnabled by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        HijriCalendarView(
            state = state,
            showTopBar = showTopBar,
            pagingEnabled = pagingEnabled,
        )
        VSpace()
        HorizontalDivider()
        VSpace()

        Text("Selection mode", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            HijriSelectionMode.entries.forEach { mode ->
                TextButton(onClick = { state.selectionMode = mode }) {
                    Text(if (state.selectionMode == mode) "[${mode.name}]" else mode.name)
                }
            }
        }

        VSpace()
        Text("First day of week", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val options = listOf(
                "Sun" to Calendar.SUNDAY,
                "Mon" to Calendar.MONDAY,
                "Sat" to Calendar.SATURDAY,
            )
            options.forEach { (label, value) ->
                TextButton(onClick = { state.firstDayOfWeek = value }) {
                    Text(if (state.firstDayOfWeek == value) "[$label]" else label)
                }
            }
        }

        VSpace()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Show top bar")
            Switch(checked = showTopBar, onCheckedChange = { showTopBar = it })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Paging enabled")
            Switch(checked = pagingEnabled, onCheckedChange = { pagingEnabled = it })
        }
    }
}

@Composable
private fun VSpace() {
    Spacer(modifier = Modifier.height(8.dp))
}
