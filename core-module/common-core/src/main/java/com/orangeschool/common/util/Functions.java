package com.orangeschool.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Functions {

    private static Functions functionInstance;

    public static Functions getInstance() {
        if (functionInstance == null) {
            functionInstance = new Functions();
        }
        return functionInstance;
    }

    public String getDateTitle(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Long seconds = ChronoUnit.SECONDS.between(now, createdAt);

        if (seconds > -60) {
            return "방금 전";
        } else if (seconds > -3600) { // 24시간 이내
            return (ChronoUnit.MINUTES.between(now, createdAt) * -1) + "분 전";
        } else if (seconds > -86400) { // 하루 이내
            return (ChronoUnit.HOURS.between(now, createdAt) * -1) + "시간 전";
        } else if (seconds > -604800) { // 7일 이내
            return (ChronoUnit.DAYS.between(now, createdAt) * -1) + "일 전";
        }
        return createdAt.format(DateTimeFormatter.ofPattern("YYYY.MM.dd"));
    }
}