package com.orangeschool.support.api.dto;

import com.orangeschool.common.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원 알림 정보 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAlarmInfo {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Boolean isRead;
    private AlarmType alarmType;
    private Long alarmId;
    private LocalDateTime createdAt;
}