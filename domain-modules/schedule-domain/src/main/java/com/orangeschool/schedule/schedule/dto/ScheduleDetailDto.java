package com.orangeschool.schedule.schedule.dto;

import com.orangeschool.common.enums.ScheduleType;
import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.education.api.dto.AcademyInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 일정 상세 정보 DTO
 * 회원 정보와 학원 정보를 포함
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailDto {
    // 일정 기본 정보
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isAllDay;
    private ScheduleType scheduleType;
    private String color;
    private String memo;
    
    // 회원 정보 (API를 통해 조회)
    private MemberInfo memberInfo;
    
    // 학원 정보 (API를 통해 조회)
    private AcademyInfo academyInfo;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * ID만 포함된 간단한 DTO 생성
     */
    public static ScheduleDetailDto withIds(Long scheduleId, Long memberId, Long academyId) {
        return ScheduleDetailDto.builder()
                .id(scheduleId)
                .memberInfo(MemberInfo.builder().id(memberId).build())
                .academyInfo(academyId != null ? AcademyInfo.builder().id(academyId).build() : null)
                .build();
    }
}