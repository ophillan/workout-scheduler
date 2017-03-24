package ru.gymthing.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mart on 23/03/2017.
 */
public class DateUtil {
    public static Date addDays(Date date, int days) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateAsString = dateFormat.format(date);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String getProperDateFormat(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        return String.format("%s/%s/%s", day, month + 1, year);
    }
}
