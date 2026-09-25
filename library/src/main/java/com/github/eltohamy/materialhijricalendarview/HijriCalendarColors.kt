package com.github.eltohamy.materialhijricalendarview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** Colors used by [HijriCalendarView]. Replaces the old `mcv_*` XML color attributes. */
@Immutable
data class HijriCalendarColors(
    val headerTitleColor: Color,
    val arrowColor: Color,
    val disabledArrowColor: Color,
    val weekDayTextColor: Color,
    val dateTextColor: Color,
    val disabledDateTextColor: Color,
    val outOfMonthDateTextColor: Color,
    val selectionColor: Color,
    val selectedDateTextColor: Color,
    val todayIndicatorColor: Color,
)

object HijriCalendarDefaults {

    @Composable
    fun colors(
        headerTitleColor: Color = MaterialTheme.colorScheme.onSurface,
        arrowColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledArrowColor: Color = arrowColor.copy(alpha = 0.38f),
        weekDayTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        dateTextColor: Color = MaterialTheme.colorScheme.onSurface,
        disabledDateTextColor: Color = dateTextColor.copy(alpha = 0.38f),
        outOfMonthDateTextColor: Color = dateTextColor.copy(alpha = 0.5f),
        selectionColor: Color = MaterialTheme.colorScheme.primary,
        selectedDateTextColor: Color = MaterialTheme.colorScheme.onPrimary,
        todayIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    ): HijriCalendarColors = HijriCalendarColors(
        headerTitleColor = headerTitleColor,
        arrowColor = arrowColor,
        disabledArrowColor = disabledArrowColor,
        weekDayTextColor = weekDayTextColor,
        dateTextColor = dateTextColor,
        disabledDateTextColor = disabledDateTextColor,
        outOfMonthDateTextColor = outOfMonthDateTextColor,
        selectionColor = selectionColor,
        selectedDateTextColor = selectedDateTextColor,
        todayIndicatorColor = todayIndicatorColor,
    )
}
