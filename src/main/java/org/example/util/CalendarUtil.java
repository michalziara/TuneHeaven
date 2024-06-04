package org.example.util;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

public class CalendarUtil {
    public static Date getEndOfMonth(Date date) {
        return java.sql.Date.valueOf(YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).atEndOfMonth());
    }

    public static Date getStartOfMonth(Date date) {
        return java.sql.Date.valueOf(YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).atDay(1));
    }
}
