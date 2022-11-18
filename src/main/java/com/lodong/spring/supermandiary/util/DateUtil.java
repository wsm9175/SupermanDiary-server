package com.lodong.spring.supermandiary.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {
    public static LocalDate getNowDate() {
        LocalDate now = LocalDate.now();
        return now;
    }

    public static LocalTime getNowTime() {
        LocalTime now = LocalTime.now();
        return now;
    }

    public static LocalDateTime getNowDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now;
    }
}
