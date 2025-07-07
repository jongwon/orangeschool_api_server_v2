package com.orangeschool.orangeschoolapiserver.domain.schedule.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.SleepInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class SleepInfoDto extends CommonDto {

    private LocalTime wakeTime;
    private LocalTime sleepTime;

    public static SleepInfoDto create(SleepInfo sleepInfo) {

        SleepInfoDto sleepInfoDto = SleepInfoDto.builder()
                .wakeTime(sleepInfo.getWakeTime())
                .sleepTime(sleepInfo.getSleepTime())
                .build();

        sleepInfoDto.setCreatedAt(sleepInfo.getCreatedAt());
        sleepInfoDto.setUpdatedAt(sleepInfo.getUpdatedAt());
        sleepInfoDto.setId(sleepInfo.getId());

        return sleepInfoDto;
    }
}
