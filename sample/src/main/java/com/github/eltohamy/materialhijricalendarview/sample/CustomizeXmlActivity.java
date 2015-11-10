package com.github.eltohamy.materialhijricalendarview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CustomizeXmlActivity extends AppCompatActivity {

    @Bind(R.id.calendarView)
    MaterialHijriCalendarView widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);
        ButterKnife.bind(this);
    }

}
