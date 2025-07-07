package com.orangeschool.support.api.dto;

import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 알림 발송 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmNotificationRequest {
    private String title;
    private String content;
    private Notification notification;
    private Long memberId;
}