package com.github.eltohamy.materialhijricalendarview.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.eltohamy.materialhijricalendarview.CalendarDay;
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;
import com.github.eltohamy.materialhijricalendarview.OnDateSelectedListener;
import com.github.eltohamy.materialhijricalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Shows off the most basic usage
 */
public class BasicActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {

    private static DateFormat FORMATTER = new SimpleDateFormat("MMM d, y");

    @Bind(R.id.calendarView)
    MaterialHijriCalendarView widget;


    @Bind(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);

        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

        //Setup initial text
        textView.setText(getSelectedDatesString());
    }

    @Override
    public void onDateSelected(@NonNull MaterialHijriCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        textView.setText(getSelectedDatesString());
    }

    @Override
    public void onMonthChanged(MaterialHijriCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
        FORMATTER.setCalendar(date.getCalendar());
//        getSupportActionBar().setTitle(FORMATTER.format(date.getCalendar().getTime()));
        getSupportActionBar().setTitle(date.getCalendar().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " " + date.getCalendar().get(Calendar.DAY_OF_MONTH) + ", " + String.valueOf(date.getYear()));
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        FORMATTER.setCalendar(date.getCalendar());
//        return FORMATTER.format(date.getCalendar().getTime());
        return date.getCalendar().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " " + date.getCalendar().get(Calendar.DAY_OF_MONTH) + ", " + String.valueOf(date.getYear());

    }
}
