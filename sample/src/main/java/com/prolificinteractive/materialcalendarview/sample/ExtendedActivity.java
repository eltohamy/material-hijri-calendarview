package com.prolificinteractive.materialcalendarview.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.prolificinteractive.materialcalendarview.ExtendedMaterialCalendarView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ExtendedActivity extends ActionBarActivity {
    @InjectView(R.id.extended_mcv)
    ExtendedMaterialCalendarView extendedMcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended);
        ButterKnife.inject(this);
    }
}
