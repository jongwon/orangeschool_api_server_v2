package com.orangeschool.orangeschoolapiserver.domain.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CalendarSearchDto {

    @Schema(description = "회원 고유 아이디 목록 - 월 간 조회시", example = "[1, 2, 3, 4]")
    private List<Long> commonMemberIdList = new ArrayList<>();
    @Schema(description = "회원 고유 아이디", example = "1")
    private Long commonMemberId = 0L;
    @Schema(description = "월 시작일", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate monthStartDate;
    @Schema(description = "주 시작일", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate weekStartDate;
    @Schema(description = "일자", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dayDate;
    @Schema(description = "학교 수업 보기", example = "false")
    private Boolean schoolShow = false;
    @Schema(description = "결제일만 보기", example = "false")
    private Boolean payOnly = false;
}
