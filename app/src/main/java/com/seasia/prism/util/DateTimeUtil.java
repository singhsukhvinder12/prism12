package com.seasia.prism.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

    private DateTimeUtil() {
    }


    public static boolean checkFutureData(Calendar now, Calendar selectedCalendar) {
        return selectedCalendar.getTime().getTime() >= now.getTime().getTime();
    }

    public static String convertUtcIntoDate(String dateTimeUTC) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(dateTimeUTC);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            dateTimeUTC = dateFormatter.format(value);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
            dateTimeUTC = "00-00-0000 00:00";
        }
        return dateTimeUTC;
    }

    private static boolean checkToday(Calendar now, Calendar mSelectedCalender) {
        return mSelectedCalender.getTime().getTime() <= now.getTime().getTime();
    }

    public static boolean checkPastDay(Calendar now, Calendar selectedCalendar) {
        return selectedCalendar.getTime().getTime() < now.getTime().getTime() && selectedCalendar.get(Calendar.DAY_OF_MONTH) < now.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isTimePast(int startHour, int startMin, Calendar selectedCalendar) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        boolean isPastDay = checkPastDay(calendar, selectedCalendar);
        if (isPastDay) {
            return true;
        }
        boolean isToday = checkToday(calendar, selectedCalendar);
        return isToday && (startHour < hours || (startHour == hours && startMin < minutes));
    }

    public static String convertDateFormat(String targetFormat, String availableFormat, String date){
        DateFormat originalFormat = new SimpleDateFormat(availableFormat, Locale.US);
        DateFormat targetFormat1 = new SimpleDateFormat(targetFormat);
        Date date1 = null;
        try {
            date1 = originalFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat1.format(date1);
        return formattedDate;
    }

    public static String parseTime(String date, String inFormat, String outFormat) {
        try {
            Date date1 = new SimpleDateFormat(inFormat, Locale.getDefault()).parse(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat(outFormat, Locale.getDefault());
            return dateFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
