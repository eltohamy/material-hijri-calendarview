package com.tohamy.materialhijricalendarview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tohamy.materialhijricalendarview.CalendarDay;
import com.tohamy.materialhijricalendarview.MaterialCalendarView;
import com.tohamy.materialhijricalendarview.OnDateChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CustomizeXmlActivity extends AppCompatActivity implements OnDateChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);

        textView = (TextView) findViewById(R.id.textView);

        MaterialCalendarView widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
    }

    @Override
    public void onDateChanged(MaterialCalendarView widget, CalendarDay date) {
        textView.setText(FORMATTER.format(date.getDate()));
    }

}
