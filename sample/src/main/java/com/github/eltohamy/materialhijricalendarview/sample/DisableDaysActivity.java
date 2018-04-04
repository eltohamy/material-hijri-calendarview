package com.github.eltohamy.materialhijricalendarview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.eltohamy.materialhijricalendarview.CalendarDay;
import com.github.eltohamy.materialhijricalendarview.DayViewDecorator;
import com.github.eltohamy.materialhijricalendarview.DayViewFacade;
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Show off setting min and max dates and disabling individual days
 */
public class DisableDaysActivity extends AppCompatActivity {

    @BindView(R.id.calendarView)
    MaterialHijriCalendarView widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);

        // Add a decorator to disable prime numbered days
        widget.addDecorator(new PrimeDayDisableDecorator());
        // Add a second decorator that explicitly enables days <= 10. This will work because
        //  decorators are applied in order, and the system allows re-enabling
        widget.addDecorator(new EnableOneToTenDecorator());

        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        widget.setMinimumDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR) + 2, Calendar.OCTOBER, 31);
        widget.setMaximumDate(calendar.getTime());
    }

    private static class PrimeDayDisableDecorator implements DayViewDecorator {

        private static boolean[] PRIME_TABLE = {
                false,  // 0?
                false,
                true, // 2
                true, // 3
                false,
                true, // 5
                false,
                true, // 7
                false,
                false,
                false,
                true, // 11
                false,
                true, // 13
                false,
                false,
                false,
                true, // 17
                false,
                true, // 19
                false,
                false,
                false,
                true, // 23
                false,
                false,
                false,
                false,
                false,
                true, // 29
                false,
                true, // 31
                false,
                false,
                false, //PADDING
        };

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return PRIME_TABLE[day.getDay()];
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }

    private static class EnableOneToTenDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.getDay() <= 10;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);
        }
    }
}
