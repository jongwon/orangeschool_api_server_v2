package com.orangeschool.orangeschoolapiserver.domain.schoolSchedule.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.schoolSchedule.entity.SchoolSchedule;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolScheduleDto extends CommonDto {

    private String keyStringValue;
    private Boolean isImportant;
    private String color;

    public static SchoolScheduleDto create(SchoolSchedule schoolSchedule) {

        SchoolScheduleDto schoolScheduleDto = SchoolScheduleDto.builder()
                .keyStringValue(schoolSchedule.getKeyStringValue())
                .isImportant(schoolSchedule.getIsImportant())
                .color(schoolSchedule.getColor())
                .build();

        schoolScheduleDto.setCreatedAt(schoolSchedule.getCreatedAt());
        schoolScheduleDto.setUpdatedAt(schoolSchedule.getUpdatedAt());
        schoolScheduleDto.setId(schoolSchedule.getId());

        return schoolScheduleDto;
    }
}
