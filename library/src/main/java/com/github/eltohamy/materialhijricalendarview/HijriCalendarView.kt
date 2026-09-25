package com.github.eltohamy.materialhijricalendarview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.eltohamy.materialhijricalendarview.format.DayFormatter
import com.github.eltohamy.materialhijricalendarview.format.TitleFormatter
import com.github.eltohamy.materialhijricalendarview.format.WeekDayFormatter
import java.util.Calendar
import kotlinx.coroutines.launch

/**
 * A Material 3, Compose-native Hijri (Umm al-Qura) calendar: a paged month grid with a
 * previous/next/title header, driven by a [HijriCalendarState].
 *
 * This is the Compose replacement for the old `MaterialHijriCalendarView` custom [android.view.View].
 * Configuration that used to be XML attributes (`mcv_*`) or imperative setters is now plain
 * composable parameters and [HijriCalendarState].
 *
 * @param onDateClick called whenever a selectable day is tapped, after [state] has applied the tap
 * according to its [HijriCalendarState.selectionMode]. Useful for side effects (e.g. closing a dialog).
 */
@Composable
fun HijriCalendarView(
    state: HijriCalendarState,
    modifier: Modifier = Modifier,
    showOtherDates: HijriShowOtherDates = HijriShowOtherDates.Defaults,
    colors: HijriCalendarColors = HijriCalendarDefaults.colors(),
    titleFormatter: TitleFormatter = TitleFormatter.Default,
    weekDayFormatter: WeekDayFormatter = WeekDayFormatter.Default,
    dayFormatter: DayFormatter = DayFormatter.Default,
    decorators: List<HijriDayDecorator> = emptyList(),
    showTopBar: Boolean = true,
    showTodayIndicator: Boolean = true,
    pagingEnabled: Boolean = true,
    tileSize: Dp = 44.dp,
    onDateClick: ((CalendarDay) -> Unit)? = null,
) {
    Column(modifier = modifier) {
        if (showTopBar) {
            HijriCalendarHeader(
                state = state,
                colors = colors,
                titleFormatter = titleFormatter,
            )
        }
        HijriWeekDayRow(
            firstDayOfWeek = state.firstDayOfWeek,
            colors = colors,
            weekDayFormatter = weekDayFormatter,
            tileSize = tileSize,
        )
        HorizontalPager(
            state = state.pagerState,
            userScrollEnabled = pagingEnabled,
        ) { page ->
            val month = remember(page) { state.monthAt(page) }
            HijriMonthGrid(
                month = month,
                state = state,
                showOtherDates = showOtherDates,
                colors = colors,
                dayFormatter = dayFormatter,
                decorators = decorators,
                showTodayIndicator = showTodayIndicator,
                tileSize = tileSize,
                onDateClick = onDateClick,
            )
        }
    }
}

@Composable
private fun HijriCalendarHeader(
    state: HijriCalendarState,
    colors: HijriCalendarColors,
    titleFormatter: TitleFormatter,
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { scope.launch { state.animateToMonth(state.monthAt(state.pagerState.currentPage - 1)) } },
            enabled = state.canGoBack,
        ) {
            Icon(
                painter = painterResource(R.drawable.mcv_action_previous),
                contentDescription = "Previous month",
                tint = if (state.canGoBack) colors.arrowColor else colors.disabledArrowColor,
            )
        }
        Text(
            text = titleFormatter.format(state.currentMonth).toString(),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = colors.headerTitleColor,
            style = MaterialTheme.typography.titleMedium,
        )
        IconButton(
            onClick = { scope.launch { state.animateToMonth(state.monthAt(state.pagerState.currentPage + 1)) } },
            enabled = state.canGoForward,
        ) {
            Icon(
                painter = painterResource(R.drawable.mcv_action_next),
                contentDescription = "Next month",
                tint = if (state.canGoForward) colors.arrowColor else colors.disabledArrowColor,
            )
        }
    }
}

@Composable
private fun HijriWeekDayRow(
    firstDayOfWeek: Int,
    colors: HijriCalendarColors,
    weekDayFormatter: WeekDayFormatter,
    tileSize: Dp,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (i in 0 until 7) {
            val dayOfWeek = ((firstDayOfWeek - 1 + i) % 7) + 1
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(tileSize),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = weekDayFormatter.format(dayOfWeek).toString(),
                    color = colors.weekDayTextColor,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
private fun HijriMonthGrid(
    month: CalendarDay,
    state: HijriCalendarState,
    showOtherDates: HijriShowOtherDates,
    colors: HijriCalendarColors,
    dayFormatter: DayFormatter,
    decorators: List<HijriDayDecorator>,
    showTodayIndicator: Boolean,
    tileSize: Dp,
    onDateClick: ((CalendarDay) -> Unit)?,
) {
    val days = remember(month, state.firstDayOfWeek) { monthGridDays(month, state.firstDayOfWeek) }
    val today = remember { CalendarDay.today() }
    Column(modifier = Modifier.fillMaxWidth()) {
        for (week in days.chunked(7)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (day in week) {
                    key(day) {
                        HijriDayCell(
                            day = day,
                            month = month,
                            state = state,
                            showOtherDates = showOtherDates,
                            colors = colors,
                            dayFormatter = dayFormatter,
                            decorators = decorators,
                            isToday = showTodayIndicator && day == today,
                            tileSize = tileSize,
                            modifier = Modifier.weight(1f),
                            onDateClick = onDateClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HijriDayCell(
    day: CalendarDay,
    month: CalendarDay,
    state: HijriCalendarState,
    showOtherDates: HijriShowOtherDates,
    colors: HijriCalendarColors,
    dayFormatter: DayFormatter,
    decorators: List<HijriDayDecorator>,
    isToday: Boolean,
    tileSize: Dp,
    modifier: Modifier = Modifier,
    onDateClick: ((CalendarDay) -> Unit)?,
) {
    val isInMonth = day.year == month.year && day.month == month.month
    val isInRange = day.isInRange(state.minDate, state.maxDate)
    val style = remember(day, decorators) {
        mergeDayStyles(decorators.mapNotNull { it.decorate(day) })
    }
    val isDecoratedDisabled = style?.enabled == false

    val enabled = isInMonth && isInRange && !isDecoratedDisabled
    var visible = enabled
    if (!isInMonth && showOtherDates.otherMonths) visible = true
    if (!isInRange && (showOtherDates.outOfRange || showOtherDates.otherMonths)) visible = visible || isInMonth
    if (isDecoratedDisabled && showOtherDates.decoratedDisabled) visible = visible || (isInMonth && isInRange)

    val isSelected = state.isSelected(day)
    val clickable = enabled && state.selectionMode != HijriSelectionMode.NONE

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .semantics {
                role = Role.Button
                this.selected = isSelected
            }
            .let {
                if (clickable) {
                    it.clickable {
                        state.toggleDate(day)
                        onDateClick?.invoke(day)
                    }
                } else it
            },
        contentAlignment = Alignment.Center,
    ) {
        if (!visible) return@Box

        val backgroundColor = when {
            isSelected -> style?.backgroundColor ?: colors.selectionColor
            else -> style?.backgroundColor ?: Color.Transparent
        }
        if (backgroundColor != Color.Transparent) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .background(backgroundColor),
            )
        }

        val textColor = style?.textColor ?: when {
            isSelected -> colors.selectedDateTextColor
            !enabled -> colors.disabledDateTextColor
            !isInMonth -> colors.outOfMonthDateTextColor
            else -> colors.dateTextColor
        }

        CompositionLocalProvider(LocalContentColor provides textColor) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = dayFormatter.format(day),
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium,
                )
                if (style?.dotColor != null) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(style.dotColor),
                    )
                } else if (isToday && !isSelected) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(colors.todayIndicatorColor),
                    )
                }
            }
        }
    }
}

/** 42 days (6 weeks) for [month]'s page, starting on the nearest [firstDayOfWeek] on/before the 1st. */
private fun monthGridDays(month: CalendarDay, firstDayOfWeek: Int): List<CalendarDay> {
    val calendar = month.calendar
    calendar.firstDayOfWeek = firstDayOfWeek
    val dow = calendar.get(Calendar.DAY_OF_WEEK)
    val delta = Math.floorMod(dow - firstDayOfWeek, 7)
    calendar.add(Calendar.DATE, -delta)
    return List(42) {
        CalendarDay.from(calendar).also { calendar.add(Calendar.DATE, 1) }
    }
}
