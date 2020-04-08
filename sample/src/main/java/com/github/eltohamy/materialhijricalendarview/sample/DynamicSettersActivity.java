package com.github.eltohamy.materialhijricalendarview.sample;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.eltohamy.materialhijricalendarview.CalendarDay;
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;
import com.github.eltohamy.materialhijricalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class DynamicSettersActivity extends AppCompatActivity {

    private static final int[] DAYS_OF_WEEK = {
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
    };
    @BindView(R.id.calendarView)
    MaterialHijriCalendarView widget;
    Random random = new Random();
    private int currentTileSize;
    private SimpleCalendarDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_setters);
        ButterKnife.bind(this);

        currentTileSize = MaterialHijriCalendarView.DEFAULT_TILE_SIZE_DP;
    }

    @OnClick(R.id.button_other_dates)
    void onOtherDatesClicked() {
        CharSequence[] items = {
                "Other Months",
                "Out Of Range",
                "Decorated Disabled"
        };
        final int[] itemValues = {
                MaterialHijriCalendarView.SHOW_OTHER_MONTHS,
                MaterialHijriCalendarView.SHOW_OUT_OF_RANGE,
                MaterialHijriCalendarView.SHOW_DECORATED_DISABLED,
        };
        int showOtherDates = widget.getShowOtherDates();
        boolean[] initSelections = {
                MaterialHijriCalendarView.showOtherMonths(showOtherDates),
                MaterialHijriCalendarView.showOutOfRange(showOtherDates),
                MaterialHijriCalendarView.showDecoratedDisabled(showOtherDates),
        };
        new AlertDialog.Builder(this)
                .setTitle("Show Other Dates")
                .setMultiChoiceItems(items, initSelections, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        int showOtherDates = widget.getShowOtherDates();
                        if (isChecked) {
                            //Set flag
                            showOtherDates |= itemValues[which];
                        } else {
                            //Unset flag
                            showOtherDates &= ~itemValues[which];
                        }
                        widget.setShowOtherDates(showOtherDates);
                    }
                })
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @OnCheckedChanged(R.id.check_text_appearance)
    void onTextAppearanceChecked(boolean checked) {
        if (checked) {
            widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large);
            widget.setDateTextAppearance(R.style.TextAppearance_AppCompat_Medium);
            widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        } else {
            widget.setHeaderTextAppearance(R.style.TextAppearance_MaterialCalendarWidget_Header);
            widget.setDateTextAppearance(R.style.TextAppearance_MaterialCalendarWidget_Date);
            widget.setWeekDayTextAppearance(R.style.TextAppearance_MaterialCalendarWidget_WeekDay);
        }
        widget.setShowOtherDates(checked ? MaterialHijriCalendarView.SHOW_ALL : MaterialHijriCalendarView.SHOW_NONE);
    }

    @OnCheckedChanged(R.id.check_page_enabled)
    void onPageEnabledChecked(boolean checked) {
        widget.setPagingEnabled(checked);
    }

    @OnClick(R.id.button_min_date)
    void onMinClicked() {
        dialogFragment = SimpleCalendarDialogFragment.getInstance(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialHijriCalendarView mWidget, @NonNull CalendarDay date, boolean selected) {
                widget.setMinimumDate(date);
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "Set Min Date");
    }

    @OnClick(R.id.button_max_date)
    void onMaxClicked() {
        dialogFragment = SimpleCalendarDialogFragment.getInstance(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialHijriCalendarView mWidget, @NonNull CalendarDay date, boolean selected) {
                widget.setMaximumDate(date);
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "Set Max Date");

    }

    @OnClick(R.id.button_selected_date)
    void onSelectedClicked() {
        dialogFragment = SimpleCalendarDialogFragment.getInstance(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialHijriCalendarView mWidget, @NonNull CalendarDay date, boolean selected) {
                widget.setSelectedDate(date);
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "Set Selected Date");

    }

    @OnClick(R.id.button_toggle_topbar)
    void onToggleTopBarClicked() {
        widget.setTopbarVisible(!widget.getTopbarVisible());
    }

    @OnClick(R.id.button_set_colors)
    void onColorsClicked() {
        int color = Color.HSVToColor(new float[]{
                random.nextFloat() * 360,
                1f,
                0.75f
        });
        widget.setArrowColor(color);
        widget.setSelectionColor(color);
    }

    @OnClick(R.id.button_set_tile_size)
    void onTileSizeClicked() {
        final NumberPicker view = new NumberPicker(this);
        view.setMinValue(24);
        view.setMaxValue(64);
        view.setWrapSelectorWheel(false);
        view.setValue(currentTileSize);
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        currentTileSize = view.getValue();
                        widget.setTileSizeDp(currentTileSize);
                    }
                })
                .show();
    }

    @OnClick(R.id.button_clear_selection)
    void onClearSelection() {
        widget.clearSelection();
    }

    @OnClick(R.id.button_selection_mode)
    void onChangeSelectionMode() {
        CharSequence[] items = {
                "No Selection",
                "Single Date",
                "Multiple Dates"
        };
        new AlertDialog.Builder(this)
                .setTitle("Selection Mode")
                .setSingleChoiceItems(items, widget.getSelectionMode(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        widget.setSelectionMode(which);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.button_set_first_day)
    void onFirstDayOfWeekClicked() {
        int index = random.nextInt(DAYS_OF_WEEK.length);
        widget.setFirstDayOfWeek(DAYS_OF_WEEK[index]);
    }


    public static class SimpleCalendarDialogFragment extends DialogFragment {

        private TextView textView;
        private OnDateSelectedListener listener;

        public static SimpleCalendarDialogFragment getInstance(OnDateSelectedListener listener) {
            SimpleCalendarDialogFragment dialogFragment = new SimpleCalendarDialogFragment();
            dialogFragment.setOnDateSelectedListener(listener);
            dialogFragment.setArguments(new Bundle());
            return dialogFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_basic, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            textView = (TextView) view.findViewById(R.id.textView);

            MaterialHijriCalendarView widget = (MaterialHijriCalendarView) view.findViewById(R.id.calendarView);

            widget.setOnDateChangedListener(listener);
        }


        public void setOnDateSelectedListener(OnDateSelectedListener listener) {
            this.listener = listener;
        }
    }
}
