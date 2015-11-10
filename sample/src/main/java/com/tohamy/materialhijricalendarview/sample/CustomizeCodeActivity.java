package com.tohamy.materialhijricalendarview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.tohamy.materialhijricalendarview.MaterialCalendarView;
import com.tohamy.materialhijricalendarview.format.ArrayWeekDayFormatter;
import com.tohamy.materialhijricalendarview.format.MonthArrayTitleFormatter;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CustomizeCodeActivity extends AppCompatActivity {

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);

        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        widget.setArrowColor(getResources().getColor(R.color.sample_primary));
        widget.setLeftArrowMask(getResources().getDrawable(R.drawable.ic_navigation_arrow_back));
        widget.setRightArrowMask(getResources().getDrawable(R.drawable.ic_navigation_arrow_forward));
        widget.setSelectionColor(getResources().getColor(R.color.sample_primary));
        widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setDateTextAppearance(R.style.CustomDayTextAppearance);
        widget.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        widget.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        widget.setTileSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics()));
        widget.setFirstDayOfWeek(Calendar.THURSDAY);
    }

}
