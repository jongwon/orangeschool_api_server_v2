package com.orangeschool.schedule.api.service;

import com.orangeschool.schedule.api.dto.ScheduleInfo;
import java.time.LocalDate;
import java.util.List;

/**
 * 일정 정보 제공 인터페이스
 */
public interface ScheduleInfoProvider {
    
    /**
     * 특정 회원의 일정 목록 조회
     * @param memberId 회원 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 일정 목록
     */
    List<ScheduleInfo> getMemberSchedules(Long memberId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 특정 학원의 일정 목록 조회
     * @param academyId 학원 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 일정 목록
     */
    List<ScheduleInfo> getAcademySchedules(Long academyId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 일정 존재 여부 확인
     * @param scheduleId 일정 ID
     * @return 존재 여부
     */
    boolean existsSchedule(Long scheduleId);
}