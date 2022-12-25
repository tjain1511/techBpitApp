package com.indianapp.techbpit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    public static int getYearFromMillis(long millis) {
        Date date = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getHourFromMillis(long millis) {
        Date date = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteFromMillis(long millis) {
        Date date = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getAmPmFromMillis(long millis) {
        Date date = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.AM_PM);
    }

    public static int getMonthFromMillis(long millis) {
        Date date = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDayFromMillis(long millis) {
        Date date = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM");
        return newFormat.format(new Date(dateTime));
    }

    public static String getFormattedDate(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM YY");
        return newFormat.format(new Date(dateTime));
    }

    public static String getDateFromFormattedDate(String args) {

        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            DateFormat targetFormat = new SimpleDateFormat("dd");
            Date date = originalFormat.parse(args);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMonthFromFormattedDate(String args) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            DateFormat targetFormat = new SimpleDateFormat("MMM");
            Date date = originalFormat.parse(args);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getFormattedTimeSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("hh:mm aa");
        return newFormat.format(new Date(dateTime));
    }
}
