package com.github.eltohamy.materialhijricalendarview.sample;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.github.eltohamy.materialhijricalendarview.CalendarDay;
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;
import com.github.eltohamy.materialhijricalendarview.OnDateSelectedListener;
import com.github.eltohamy.materialhijricalendarview.sample.decorators.EventDecorator;
import com.github.eltohamy.materialhijricalendarview.sample.decorators.HighlightWeekendsDecorator;
import com.github.eltohamy.materialhijricalendarview.sample.decorators.MySelectorDecorator;
import com.github.eltohamy.materialhijricalendarview.sample.decorators.OneDayDecorator;
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Shows off the most basic usage
 */
public class BasicActivityDecorated extends AppCompatActivity implements OnDateSelectedListener {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @BindView(R.id.calendarView)
    MaterialHijriCalendarView widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);

        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialHijriCalendarView.SHOW_ALL);

        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        widget.setMinimumDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        widget.setMaximumDate(calendar.getTime());

        widget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    @Override
    public void onDateSelected(@NonNull MaterialHijriCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                UmmalquraCalendar ummalquraCalendar = new UmmalquraCalendar();
                ummalquraCalendar.setTime(calendar.getTime());
                CalendarDay day = CalendarDay.from(ummalquraCalendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            widget.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }
}
