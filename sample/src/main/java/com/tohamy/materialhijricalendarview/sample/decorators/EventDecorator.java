package com.tohamy.materialhijricalendarview.sample.decorators;

import com.tohamy.materialhijricalendarview.CalendarDay;
import com.tohamy.materialhijricalendarview.DayViewDecorator;
import com.tohamy.materialhijricalendarview.DayViewFacade;
import com.tohamy.materialhijricalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
    }
}
