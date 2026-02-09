package com.disha.fintrack.util;

import java.time.LocalDate;
import java.time.YearMonth;

public class MonthDateMapper {

//    converting month and year to start and end date
    public static LocalDate[] getStartAndEndDate(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return new LocalDate[]{startDate, endDate};
    }


}
