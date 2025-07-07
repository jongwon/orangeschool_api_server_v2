package com.orangeschool.schedule.schedule.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 리팩토링된 Schedule Entity
 * 다른 도메인의 Entity 직접 참조를 제거하고 ID만 저장
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "schedule")
public class ScheduleRefactored extends BaseEntity {
    
    // CommonMember 대신 ID만 저장
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    
    @Builder.Default
    private Boolean isAllDay = false;
    
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;
    
    // 학원 관련 정보 (ID만 저장)
    @Column(name = "academy_id")
    private Long academyId;
    
    private String academyName;
    private String color;
    
    @Column(columnDefinition = "TEXT")
    private String memo;
    
    // 반복 정보
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CycleType cycleType = CycleType.NONE;
    
    private LocalDate cycleEndDate;
    
    // 결제 정보
    private LocalDate payDate;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PayCycle payCycle = PayCycle.NONE;
    
    private LocalDate payCycleEndDate;
    
    @Builder.Default
    private Long amount = 0L;
    
    @Builder.Default
    private Boolean usePaymentAlarm = false;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PayAlarmType payAlarmType = PayAlarmType.NONE;
    
    // 알람 설정
    @Builder.Default
    private Boolean useAlarm = false;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ScheduleAlarmType scheduleAlarmType = ScheduleAlarmType.NONE;
    
    @Builder.Default
    private Boolean isDelete = false;
    
    // 업데이트 메서드
    public void update(UpdateScheduleDto dto) {
        this.title = dto.getTitle();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.isAllDay = dto.getIsAllDay();
        this.scheduleType = dto.getScheduleType();
        this.color = dto.getColor();
        this.memo = dto.getMemo();
    }
    
    public void delete() {
        this.isDelete = true;
    }
}