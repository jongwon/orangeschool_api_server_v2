package com.orangeschool.support.memberAlarm.service;

import com.google.firebase.messaging.Notification;
import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.support.api.dto.AlarmNotificationRequest;
import com.orangeschool.support.api.dto.MemberAlarmInfo;
import com.orangeschool.support.api.service.MemberAlarmProvider;
import com.orangeschool.support.memberAlarm.dto.MemberAlarmDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberAlarmProviderImpl implements MemberAlarmProvider {

    private final MemberAlarmService memberAlarmService;
    private final MemberInfoProvider memberInfoProvider;

    @Override
    @Transactional
    public void createSystemAlarm(AlarmNotificationRequest request) throws Exception {
        var memberInfo = memberInfoProvider.getMemberInfo(request.getMemberId());
        
        if (memberInfo == null) {
            throw new IllegalArgumentException("Member not found: " + request.getMemberId());
        }
        
        // MemberAlarmService의 createToSystem 메서드를 호출하기 위해 CommonMember 객체가 필요
        // 하지만 API 모듈에서는 엔티티에 직접 접근할 수 없으므로, 
        // MemberAlarmService를 수정하여 memberId를 받도록 하거나 새로운 메서드를 추가해야 함
        memberAlarmService.createToSystemByMemberId(
            request.getTitle(), 
            request.getContent(), 
            request.getNotification(), 
            request.getMemberId()
        );
    }

    @Override
    @Transactional
    public void createSystemAlarm(String title, String content, Long memberId) throws Exception {
        var notification = Notification.builder()
                .setTitle(title)
                .setBody("새로운 알림이 도착했어요.")
                .build();
                
        createSystemAlarm(AlarmNotificationRequest.builder()
                .title(title)
                .content(content)
                .notification(notification)
                .memberId(memberId)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberAlarmInfo> getMemberAlarms(Long memberId, Pageable pageable) throws Exception {
        Page<MemberAlarmDto> alarmDtos = memberAlarmService.get(memberId, pageable);
        
        return alarmDtos.map(dto -> MemberAlarmInfo.builder()
                .id(dto.getId())
                .memberId(memberId)
                .title(dto.getTitle())
                .content(dto.getContent())
                .isRead(dto.getIsRead())
                .alarmType(dto.getAlarmType())
                .alarmId(dto.getAlarmId())
                .createdAt(dto.getCreatedAt())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean hasNewAlarm(Long memberId) throws Exception {
        return memberAlarmService.getNewExist(memberId);
    }

    @Override
    @Transactional
    public MemberAlarmInfo markAsRead(Long alarmId) throws Exception {
        MemberAlarmDto dto = memberAlarmService.getById(alarmId);
        
        return MemberAlarmInfo.builder()
                .id(dto.getId())
                .memberId(dto.getCommonMemberId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .isRead(dto.getIsRead())
                .alarmType(dto.getAlarmType())
                .alarmId(dto.getAlarmId())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}