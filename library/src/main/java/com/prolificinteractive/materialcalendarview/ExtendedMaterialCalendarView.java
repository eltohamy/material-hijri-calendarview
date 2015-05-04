package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtendedMaterialCalendarView extends FrameLayout implements OnDateChangedListener {

    final static SimpleDateFormat fullDayFormat = new SimpleDateFormat("EEEE");
    final static SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
    final static SimpleDateFormat dayFormat = new SimpleDateFormat("d");
    final static SimpleDateFormat yearFormat = new SimpleDateFormat("y");

    final MaterialCalendarView mcv;
    final TextView fullDay;
    final TextView month;
    final TextView day;
    final TextView year;

    public ExtendedMaterialCalendarView(Context context) {
        this(context, null);
    }

    public ExtendedMaterialCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final View view = LayoutInflater.from(context).inflate(R.layout.extended_view, null, false);

        mcv = (MaterialCalendarView) view.findViewById(R.id.mcv);
        fullDay = (TextView) view.findViewById(R.id.full_day);
        month = (TextView) view.findViewById(R.id.month);
        day = (TextView) view.findViewById(R.id.day);
        year = (TextView) view.findViewById(R.id.year);

        addView(view);

        mcv.setOnDateChangedListener(this);
    }

    public MaterialCalendarView getMaterialCalendarView() {
        return mcv;
    }

    public void setSelectedDate(final Date date) {
        fullDay.setText(fullDayFormat.format(date));
        month.setText(monthFormat.format(date));
        day.setText(dayFormat.format(date));
        year.setText(yearFormat.format(date));
    }

    @Override
    public void onDateChanged(MaterialCalendarView widget, CalendarDay date) {
        setSelectedDate(date.getDate());
    }
}
