package com.github.eltohamy.materialhijricalendarview

import androidx.compose.runtime.Immutable

/**
 * Controls which non-selectable days are still shown (as opposed to left blank).
 * Replaces the old `mcv_showOtherDates` bit-flags attribute.
 */
@Immutable
data class HijriShowOtherDates(
    /** Show leading/trailing days that belong to the previous/next month. */
    val otherMonths: Boolean = false,
    /** Show days outside [HijriCalendarState.minDate]..[HijriCalendarState.maxDate]. */
    val outOfRange: Boolean = false,
    /** Show days a [HijriDayDecorator] disabled via [HijriDayStyle.enabled]. */
    val decoratedDisabled: Boolean = true,
) {
    companion object {
        val None = HijriShowOtherDates(otherMonths = false, outOfRange = false, decoratedDisabled = false)
        val Defaults = HijriShowOtherDates(decoratedDisabled = true)
        val All = HijriShowOtherDates(otherMonths = true, outOfRange = true, decoratedDisabled = true)
    }
}
