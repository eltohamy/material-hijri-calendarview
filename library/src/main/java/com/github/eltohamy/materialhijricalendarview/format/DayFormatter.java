package com.github.eltohamy.materialhijricalendarview.format;

import android.support.annotation.NonNull;

import com.github.eltohamy.materialhijricalendarview.CalendarDay;

import java.text.SimpleDateFormat;

/**
 * Supply labels for a given day. Default implementation is to format using a {@linkplain SimpleDateFormat}
 */
public interface DayFormatter {

    /**
     * Format a given day into a string
     *
     * @param day the day
     * @return a label for the day
     */
    @NonNull
    String format(@NonNull CalendarDay day);

    /**
     * Default implementation used by {@linkplain com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView}
     */
    public static final DayFormatter DEFAULT = new DateFormatDayFormatter();
}
