package ru.p03.snpa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    // dd.MM.yyyy
    public static Date getDateFromString(String dateString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.parse(dateString);
    }

    // dd.MM.yyyy
    public static String getStringFromDate(Date date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }

    public static Date fromYYMMDD(int year, int month, int day){

        System.out.println(year);

        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, 0, 0);

        return c.getTime();
    }

    public static final Date MIN_DATE = DateUtils.fromYYMMDD(1000, 1, 1);
    public static final Date MAX_DATE = DateUtils.fromYYMMDD(2999, 12, 31);

    public static final Date MIN_DATE_PLUS_DAY = DateUtils.fromYYMMDD(1000, 1, 2);
    public static final Date MAX_DATE_MINUS_DAY = DateUtils.fromYYMMDD(2999, 12, 30);

}
