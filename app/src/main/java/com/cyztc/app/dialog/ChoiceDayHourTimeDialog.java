package com.cyztc.app.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.widget.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.cyztc.app.widget.wheel.widget.views.OnWheelChangedListener;
import com.cyztc.app.widget.wheel.widget.views.OnWheelScrollListener;
import com.cyztc.app.widget.wheel.widget.views.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ywl on 2017-5-20.
 */

public class ChoiceDayHourTimeDialog extends BaseDialog{

    private Button btnYes;
    private Button btnNo;

    private WheelView wvYear;
    private WheelView wvMonth;
    private WheelView wvDay;
    private WheelView wvHour;
    private WheelView wvMin;

    private ArrayList<String> arry_years = new ArrayList<String>();
    private ArrayList<String> arry_months = new ArrayList<String>();
    private ArrayList<String> arry_days = new ArrayList<String>();
    private ArrayList<String> arry_hours = new ArrayList<>();
    private ArrayList<String> arry_mins = new ArrayList<>();
    private CalendarTextAdapter mYearAdapter;
    private CalendarTextAdapter mMonthAdapter;
    private CalendarTextAdapter mDaydapter;
    private CalendarTextAdapter mHourAdapter;
    private CalendarTextAdapter mMinAdapter;

    private int currentYear = getYear();
    private int currentMonth = getMonth();
    private int currentDay = getDay();

    private int month;
    private int day;

    private String selectYear;
    private String selectMonth;
    private String selectDay;
    private String selectHour;
    private String selectMin;

    private boolean issetdata = false;

    private int maxTextSize = 18;
    private int minTextSize = 14;

    private OnBirthListener onBirthListener;


    public ChoiceDayHourTimeDialog(Context context) {
        super(context);
    }

    public void setOnBirthListener(OnBirthListener onBirthListener) {
        this.onBirthListener = onBirthListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_date_hour_layout);
        wvYear = (WheelView) findViewById(R.id.wv_year);
        wvMonth = (WheelView) findViewById(R.id.wv_month);
        wvDay = (WheelView) findViewById(R.id.wv_day);
        wvHour = (WheelView) findViewById(R.id.wv_hour);
        wvMin = (WheelView) findViewById(R.id.wv_min);
        btnYes = (Button) findViewById(R.id.yes);
        btnNo = (Button) findViewById(R.id.no);


        if (!issetdata) {
            initData();
        }
        initYears();
        mYearAdapter = new CalendarTextAdapter(context, arry_years, setYear(getYear()), maxTextSize, minTextSize);
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(mYearAdapter);
        wvYear.setCurrentItem(setYear(currentYear));

        initMonths(month);
        mMonthAdapter = new CalendarTextAdapter(context, arry_months, setMonth(getMonth()), maxTextSize, minTextSize);
        wvMonth.setVisibleItems(5);
        wvMonth.setViewAdapter(mMonthAdapter);
        wvMonth.setCurrentItem(setMonth(getMonth()));

        initDays(day);
        mDaydapter = new CalendarTextAdapter(context, arry_days, setDay(getDay()), maxTextSize, minTextSize);
        wvDay.setVisibleItems(5);
        wvDay.setViewAdapter(mDaydapter);
        wvDay.setCurrentItem(setDay(getDay()));

        initHours();
        mHourAdapter = new CalendarTextAdapter(context, arry_hours, setHours(getHour()), maxTextSize, minTextSize);
        wvHour.setVisibleItems(5);
        wvHour.setViewAdapter(mHourAdapter);
        wvHour.setCurrentItem(setHours(getHour()));

        initMins();
        mMinAdapter = new CalendarTextAdapter(context, arry_mins, setMin(getMin()), maxTextSize, minTextSize);
        wvMin.setVisibleItems(5);
        wvMin.setViewAdapter(mMinAdapter);
        wvMin.setCurrentItem(setMin(getMin()));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBirthListener != null)
                {
                    onBirthListener.onClick(
                            (String) mYearAdapter.getItemText(wvYear.getCurrentItem()),
                            (String) mMonthAdapter.getItemText(wvMonth.getCurrentItem()),
                            (String) mDaydapter.getItemText(wvDay.getCurrentItem()),
                            (String) mHourAdapter.getItemText(wvHour.getCurrentItem()),
                            (String) mMinAdapter.getItemText(wvMin.getCurrentItem()));
                }
                dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        wvYear.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                selectYear = currentText;
                setTextviewSize(currentText, mYearAdapter);
                currentYear = Integer.parseInt(currentText);
            }
        });

        wvYear.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
            }
        });

        wvMonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                selectMonth = currentText;
                setTextviewSize(currentText, mMonthAdapter);
            }
        });

        wvMonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMonthAdapter);
            }
        });

        wvDay.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
                selectDay = currentText;
            }
        });

        wvDay.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
            }
        });

        wvHour.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mHourAdapter);
                selectHour = currentText;
            }
        });

        wvHour.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mHourAdapter);
            }
        });

        wvMin.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mMinAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMinAdapter);
                selectMin = currentText;
            }
        });

        wvMin.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMinAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMinAdapter);
            }
        });
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_date_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    public void initYears() {
        for (int i = getYear() + 2; i >= getYear() - 1; i--) {
            arry_years.add(i + "");
        }
    }


    public void initMonths(int months) {
        arry_months.clear();
        for (int i = 1; i <= 12; i++) {
            if(i < 10)
            {
                arry_months.add("0" + i);
            }
            else {
                arry_months.add(i + "");
            }
        }
    }

    public void initDays(int days) {
        arry_days.clear();
        for (int i = 1; i <= 31; i++) {
            if(i < 10)
            {
                arry_days.add("0" + i);
            }
            else
            {
                arry_days.add(i + "");
            }

        }
    }

    public void initHours()
    {
        for(int i = 0; i < 24; i++)
        {
            if(i < 10) {
                arry_hours.add("0" + i);
            }
            else
            {
                arry_hours.add(i + "");
            }
        }
    }

    public void initMins()
    {
        for(int i = 0; i < 60; i++)
        {
            if(i < 10) {
                arry_mins.add("0" + i);
            }
            else
            {
                arry_mins.add(i + "");
            }
        }
    }

    public void initData() {
        setDate(getYear(), getMonth(), getDay());
        this.currentDay = 1;
        this.currentMonth = 1;
    }

    public int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    public int getHour()
    {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public int getMin()
    {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MINUTE);
    }

    public int setHours(int hour)
    {
        int hoursindex = 0;
        for(int i = 0; i < arry_hours.size(); i++)
        {
            if(arry_hours.get(i).equals(hour + ""))
            {
                return hoursindex;
            }
            hoursindex++;
        }
        return hoursindex;
    }

    public int setMin(int min)
    {
        int minindex = 0;
        for(int i = 0; i < arry_mins.size(); i++)
        {
            if(arry_mins.get(i).equals(min + ""))
            {
                return minindex;
            }
            minindex++;
        }
        return minindex;
    }


    /**
     * 设置年月日
     *
     * @param year
     * @param month
     * @param day
     */
    public void setDate(int year, int month, int day) {
        selectYear = year + "";
        selectMonth = month + "";
        selectDay = day + "";
        issetdata = true;
        this.currentYear = year;
        this.currentMonth = month;
        this.currentDay = day;
        if (year == getYear()) {
            this.month = getMonth();
        } else {
            this.month = 12;
        }
        calDays(year, month);
    }

    /**
     * 设置年份
     *
     * @param year
     */
    public int setYear(int year) {
        int yearIndex = 0;
        if (year != getYear()) {
            this.month = 12;
        } else {
            this.month = getMonth();
        }
        for (int i = getYear() + 2; i >= getYear() - 1; i--) {
            if (i == year) {
                return yearIndex;
            }
            yearIndex++;
        }
        return yearIndex;
    }

    /**
     * 设置月份
     *
     * @param month
     * @return
     */
    public int setMonth(int month) {
        int monthIndex = 0;
        calDays(currentYear, month);
        for (int i = 1; i < this.month; i++) {
            if (month == i) {
                return monthIndex;
            } else {
                monthIndex++;
            }
        }
        return monthIndex;
    }

    public int setDay(int day)
    {
        int dayindex = 0;
        for(int i = 1; i < this.day; i++)
        {
            if(day == i)
            {
                return dayindex;
            }
            dayindex++;
        }
        return dayindex;
    }

    /**
     * 计算每月多少天
     *
     * @param month
     */
    public void calDays(int year, int month) {
        boolean leayyear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.day = 31;
                    break;
                case 2:
                    if (leayyear) {
                        this.day = 29;
                    } else {
                        this.day = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    this.day = 30;
                    break;
            }
        }
        if (year == getYear() && month == getMonth()) {
            this.day = getDay();
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }

    public interface OnBirthListener {
        void onClick(String year, String month, String day, String hour, String min);
    }

}
