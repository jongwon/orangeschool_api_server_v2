package com.orangeschool.orangeschoolapiserver.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
public class DayMessageSearchDto {

    @Schema(description = "일자", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dayDate;
}
