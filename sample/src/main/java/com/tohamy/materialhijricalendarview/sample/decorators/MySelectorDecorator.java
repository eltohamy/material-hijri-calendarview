package com.tohamy.materialhijricalendarview.sample.decorators;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.tohamy.materialhijricalendarview.CalendarDay;
import com.tohamy.materialhijricalendarview.DayViewDecorator;
import com.tohamy.materialhijricalendarview.DayViewFacade;
import com.tohamy.materialhijricalendarview.sample.R;

/**
 * Use a custom selector
 */
public class MySelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public MySelectorDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.my_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
