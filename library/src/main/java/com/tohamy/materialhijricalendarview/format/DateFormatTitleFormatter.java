package com.tohamy.materialhijricalendarview.format;

import com.tohamy.materialhijricalendarview.CalendarDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Format using a {@linkplain java.text.DateFormat} instance.
 */
public class DateFormatTitleFormatter implements TitleFormatter {

    private final SimpleDateFormat dateFormat;

    /**
     * Format using "MMMM yyyy" for formatting
     */
    public DateFormatTitleFormatter() {
        this.dateFormat = new SimpleDateFormat(
                "MMMM yyyy", Locale.getDefault()
        );
    }

    /**
     * Format using a specified {@linkplain DateFormat}
     *
     * @param format the format to use
     */
    public DateFormatTitleFormatter(SimpleDateFormat format) {
        this.dateFormat = format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence format(CalendarDay day) {
        dateFormat.setCalendar(day.getCalendar());
        return dateFormat.format(day.getCalendar().getTime());
    }
}
