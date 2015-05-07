package com.prolificinteractive.materialcalendarview;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtendedMaterialCalendarView extends FrameLayout implements OnDateChangedListener {

    private final static SimpleDateFormat FULL_DAY_FORMAT = new SimpleDateFormat("EEEE");
    private final static SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMM");
    private final static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("d");
    private final static SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("y");

    private final MaterialCalendarView mcv;
    private final TextView fullDay;
    private final TextView month;
    private final TextView day;
    private final TextView year;

    private OnDateChangedListener onDateChangedListener;
    private CalendarDay previousDay;

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

    public void setSelectedDate(final CalendarDay calendarDay) {
        final Date date = calendarDay.getDate();
        if (previousDay == null) {
            fullDay.setText(FULL_DAY_FORMAT.format(date));
            month.setText(MONTH_FORMAT.format(date));
            day.setText(DAY_FORMAT.format(date));
            year.setText(YEAR_FORMAT.format(date));
        } else {
            // full day
            final String futureFullDay = FULL_DAY_FORMAT.format(date);
            if (!fullDay.getText().toString().equalsIgnoreCase(futureFullDay)) {
                animateView(fullDay, futureFullDay, calendarDay);
            }
            // month
            final String futureMonth = MONTH_FORMAT.format(date);
            if (!month.getText().toString().equalsIgnoreCase(futureMonth)) {
                animateView(month, futureMonth, calendarDay);
            }
            // day
            final String futureDay = DAY_FORMAT.format(date);
            if (!day.getText().toString().equalsIgnoreCase(futureDay)) {
                animateView(day, futureDay, calendarDay);
            }
            // year
            final String futureYear = YEAR_FORMAT.format(date);
            if (!year.getText().toString().equalsIgnoreCase(futureYear)) {
                animateView(year, futureYear, calendarDay);
            }
        }
    }

    private void animateView(final TextView view, final String text, final CalendarDay day) {

        final int yTranslation =
                getResources().getDimensionPixelSize(R.dimen.mcv_default_title_y_translation)
                        * (previousDay.isBefore(day) ? 1 : -1);
        final DecelerateInterpolator interpolator = new DecelerateInterpolator(2f);
        final int duration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        final int startDelay = getResources().getInteger(R.integer.mcv_default_title_start_delay);

        view.animate().cancel();
        view.setTranslationY(0);
        view.animate()
                .translationY(yTranslation * -1)
                .alpha(0)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setListener(new AnimatorListener() {

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        view.setTranslationY(0);
                        view.setAlpha(1);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        view.setText(text);
                        view.setTranslationY(yTranslation);
                        view.animate()
                                .translationY(0)
                                .alpha(1)
                                .setDuration(duration)
                                .setInterpolator(interpolator)
                                .setListener(new AnimatorListener())
                                .start();
                    }
                }).start();
    }

    public void setOnDateChangedListener(final OnDateChangedListener listener) {
        this.onDateChangedListener = listener;
    }

    @Override
    public void onDateChanged(MaterialCalendarView widget, CalendarDay date) {
        setSelectedDate(date);
        if (onDateChangedListener != null)
            onDateChangedListener.onDateChanged(widget, date);
        previousDay = date;
    }
}
