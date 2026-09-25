package com.github.eltohamy.materialhijricalendarview

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Visual overrides for a single day, produced by a [HijriDayDecorator]. Any `null` field keeps
 * whatever the calendar would otherwise use for that day.
 *
 * This is the Compose-idiomatic replacement for the old `DayViewDecorator` / `DayViewFacade`
 * (Drawable + Spannable based) API: instead of mutating a facade, a decorator returns the style
 * it wants applied.
 */
@Immutable
data class HijriDayStyle(
    /** false disables (and, depending on [MaterialHijriCalendarView.ShowOtherDates] flags, hides) the day. */
    val enabled: Boolean = true,
    val backgroundColor: Color? = null,
    val textColor: Color? = null,
    /** Small dot drawn under the day label, e.g. to mark an event. */
    val dotColor: Color? = null,
)

/**
 * Examines a day and optionally returns styling to apply to it. Return `null` to leave the day
 * untouched. When several decorators are supplied, later ones take precedence field-by-field over
 * earlier ones (matching the original library's decorator ordering).
 */
fun interface HijriDayDecorator {
    fun decorate(day: CalendarDay): HijriDayStyle?
}

/** Merges [styles] in order, with later non-null fields overriding earlier ones. */
internal fun mergeDayStyles(styles: List<HijriDayStyle>): HijriDayStyle? {
    if (styles.isEmpty()) return null
    var enabled = true
    var backgroundColor: Color? = null
    var textColor: Color? = null
    var dotColor: Color? = null
    for (style in styles) {
        if (!style.enabled) enabled = false
        style.backgroundColor?.let { backgroundColor = it }
        style.textColor?.let { textColor = it }
        style.dotColor?.let { dotColor = it }
    }
    return HijriDayStyle(enabled, backgroundColor, textColor, dotColor)
}
