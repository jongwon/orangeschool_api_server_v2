package com.orangeschool.schedule.schedule.service;

import com.orangeschool.common.enums.ScheduleType;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.education.api.service.AcademyInfoProvider;
import com.orangeschool.schedule.schedule.dto.*;
import com.orangeschool.schedule.schedule.entity.Schedule;
import com.orangeschool.schedule.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 리팩토링된 ScheduleService
 * 다른 도메인의 Repository 직접 참조를 제거하고 API 인터페이스를 사용
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ScheduleServiceRefactored {
    
    private final ScheduleRepository scheduleRepository;
    
    // Repository 대신 API 인터페이스 사용
    private final MemberInfoProvider memberInfoProvider;
    private final AcademyInfoProvider academyInfoProvider;
    
    /**
     * 일정 생성
     */
    public ScheduleDto createSchedule(CreateScheduleDto dto) {
        // 회원 존재 여부 확인 (Repository 직접 참조 대신 API 사용)
        if (!memberInfoProvider.existsMember(dto.getMemberId())) {
            throw new CustomException(ResponseCode.USER_NOT_FOUND);
        }
        
        // 학원 일정인 경우 학원 존재 여부 확인
        if (dto.getScheduleType() == ScheduleType.ACADEMY && dto.getAcademyId() != null) {
            if (!academyInfoProvider.existsAcademy(dto.getAcademyId())) {
                throw new CustomException(ResponseCode.ACADEMY_NOT_FOUND);
            }
        }
        
        Schedule schedule = Schedule.builder()
                .memberId(dto.getMemberId())  // Entity 참조 대신 ID만 저장
                .academyId(dto.getAcademyId())
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .isAllDay(dto.getIsAllDay())
                .scheduleType(dto.getScheduleType())
                .color(dto.getColor())
                .memo(dto.getMemo())
                .build();
                
        Schedule saved = scheduleRepository.save(schedule);
        return convertToDto(saved);
    }
    
    /**
     * 회원의 일정 목록 조회
     */
    public List<ScheduleDto> getMemberSchedules(Long memberId, LocalDate startDate, LocalDate endDate) {
        // 회원 존재 여부 확인
        if (!memberInfoProvider.existsMember(memberId)) {
            throw new CustomException(ResponseCode.USER_NOT_FOUND);
        }
        
        List<Schedule> schedules = scheduleRepository.findByMemberIdAndDateRange(
            memberId, startDate, endDate
        );
        
        return schedules.stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * 일정 삭제
     */
    public void deleteSchedule(Long scheduleId, Long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ResponseCode.SCHEDULE_NOT_FOUND));
                
        // 권한 확인 (일정 소유자인지)
        if (!schedule.getMemberId().equals(memberId)) {
            throw new CustomException(ResponseCode.FORBIDDEN);
        }
        
        scheduleRepository.delete(schedule);
    }
    
    private ScheduleDto convertToDto(Schedule schedule) {
        return ScheduleDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .isAllDay(schedule.getIsAllDay())
                .scheduleType(schedule.getScheduleType())
                .color(schedule.getColor())
                .memo(schedule.getMemo())
                .memberId(schedule.getMemberId())
                .academyId(schedule.getAcademyId())
                .createdAt(schedule.getCreatedAt())
                .build();
    }
}