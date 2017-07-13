package com.app.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import trikita.log.Log;

public class DateTime {

    private int hour, minute, ampm;
    private int day, month, year;

    public DateTime() {
        Calendar c1 = Calendar.getInstance();
        setDay(c1.get(Calendar.DAY_OF_MONTH));
        setMonth(c1.get(Calendar.MONTH));
        setYear(c1.get(Calendar.YEAR));
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getAmpm() {
        return ampm;
    }

    public void setAmpm(int ampm) {
        this.ampm = ampm;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    @Override
    public String toString() {
        return "DateTime [hour=" + hour + ", minute=" + minute + ", ampm=" + ampm + ", day=" + day + ", month=" + month
                + ", year=" + year + "]";
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDateDiff(int calField, int calDiff) {
        Calendar c2 = Calendar.getInstance();
        c2.add(calField, calDiff);
        setDay(c2.get(Calendar.DAY_OF_MONTH));
        setMonth(c2.get(Calendar.MONTH));
        setYear(c2.get(Calendar.YEAR));
    }

    public Calendar getCalendarObject() {
        Calendar co = Calendar.getInstance();
        co.clear();

        int h = hour;
        if (ampm == 1)
            h += 12;

        co.set(year, month, day, h, minute);
        return co;
    }

    public String getShortMonth() {
        return months[(month + 1)].toString().substring(0, 3);
    }

    public String getYMDString() {

        String prefix = "";
        if (month <= 8)
            prefix = "0";

        String sprefix = "";
        if (day <= 9)
            sprefix = "0";

        return "" + year + "-" + prefix + (month + 1) + "-" + sprefix + day;
    }

    public String getDMonthY() {
        //e.g : 8 March 2016
        return "" + day + " " + months[month] + " " + year;
    }

    public void setDateYMD(String date) {

        try {
            Log.d("ymd date", date);
            String arr[] = date.split("-");
            Log.d("ymd", date + " : " + arr[0] + "-" + arr[1] + "-" + arr[2]);
            year = Integer.parseInt(arr[0]);
            month = Integer.parseInt(arr[1]) - 1;
            day = Integer.parseInt(arr[2]);
        } catch (Exception e) {
            LogEx.print(e);
        }
    }

    public String getYMD() {

        String a = "";

        try {
            int mm = month + 1;

            String m = "";
            if (mm < 10) m += "0";
            m += "" + mm;

            String d = "";
            if (day < 10) d += "0";
            d += "" + day;

            a = "" + year + "-" + (m) + "-" + d;
        } catch (Exception e) {
            LogEx.print(e);
        }

        return a;
    }

    public String getFormattedDate(String format) {

        String ret = "";

        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formater = new SimpleDateFormat(format);
            Date d = parser.parse(getYMD());
            ret = formater.format(d).toString();
        } catch (Exception e) {
            ret = "";
        }

        return ret;
    }

    public static String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
}
