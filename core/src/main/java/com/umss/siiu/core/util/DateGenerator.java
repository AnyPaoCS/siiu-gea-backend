package com.umss.siiu.core.util;

import java.util.Calendar;
import java.util.Date;

public class DateGenerator {

    private DateGenerator() {
        super();
    }

    public static Date addDaysToDate(int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }

}
