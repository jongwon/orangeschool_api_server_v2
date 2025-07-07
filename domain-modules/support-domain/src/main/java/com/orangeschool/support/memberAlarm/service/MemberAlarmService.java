package com.orangeschool.support.memberAlarm;

import com.google.firebase.messaging.Notification;
import com.orangeschool.common.enums.AlarmMemberType;
import com.orangeschool.common.enums.AlarmType;
import com.orangeschool.common.enums.MemberType;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.infra.util.PushMessageManagement;
import com.orangeschool.support.alarm.entity.Alarm;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
import com.orangeschool.support.memberAlarm.dto.MemberAlarmDto;
import com.orangeschool.support.memberAlarm.entity.MemberAlarm;
import com.orangeschool.support.memberAlarm.repository.MemberAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberAlarmService {

    private final CommonMemberRepository commonMemberRepository;
    private final MemberAlarmRepository memberAlarmRepository;
    private final PushMessageManagement pushMessageManagement;

    @Transactional
    public void create(Alarm alarm) throws Exception {
        List<CommonMember> memberList = new ArrayList<>();

        if (alarm.getAlarmType() == AlarmType.SERVICE) {
            if (alarm.getAlarmMemberType() == AlarmMemberType.PARENT) {
                memberList = commonMemberRepository.findByAgreeToServiceAndMemberType(true, MemberType.PARENT);
            } else {
                memberList = commonMemberRepository.findByAgreeToServiceAndMemberType(true, MemberType.CHILD);
            }
        } else if (alarm.getAlarmType() == AlarmType.AD) {
            if (alarm.getAlarmMemberType() == AlarmMemberType.PARENT) {
                memberList = commonMemberRepository.findByAgreeToAdAndMemberType(true, MemberType.PARENT);
            } else {
                memberList = commonMemberRepository.findByAgreeToAdAndMemberType(true, MemberType.CHILD);
            }
        }

        List<MemberAlarm> memberAlarmList = new ArrayList<>();
        String title = alarm.getTitle();
        String content = alarm.getContent();
        Long alarmId = alarm.getId();

        for (CommonMember member : memberList) {
            MemberAlarm memberAlarm = MemberAlarm.builder()
                    .commonMember(member)
                    .title(title)
                    .content(content)
                    .isRead(false)
                    .alarmId(alarmId)
                    .alarmType(alarm.getAlarmType())
                    .build();

            memberAlarmList.add(memberAlarm);
        }

        memberAlarmRepository.saveAll(memberAlarmList);

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody("새로운 알림이 도착했어요.")
                .build();
        if (alarm.getAlarmType() == AlarmType.SERVICE) {

            if (alarm.getAlarmMemberType() == AlarmMemberType.ALL) {
                pushMessageManagement.sendToList(commonMemberRepository.findServicePushTokenByMemberType(null), notification);
            } else  if (alarm.getAlarmMemberType() == AlarmMemberType.PARENT){
                pushMessageManagement.sendToList(commonMemberRepository.findServicePushTokenByMemberType(MemberType.PARENT), notification);
            }else {
                pushMessageManagement.sendToList(commonMemberRepository.findServicePushTokenByMemberType(MemberType.CHILD), notification);
            }
        } else if (alarm.getAlarmType() == AlarmType.AD) {
            if (alarm.getAlarmMemberType() == AlarmMemberType.ALL) {
                pushMessageManagement.sendToList(commonMemberRepository.findAdPushTokenByMemberType(null), notification);
            } else if (alarm.getAlarmMemberType() == AlarmMemberType.PARENT) {
                pushMessageManagement.sendToList(commonMemberRepository.findAdPushTokenByMemberType(MemberType.PARENT), notification);
            } else{
                pushMessageManagement.sendToList(commonMemberRepository.findAdPushTokenByMemberType(MemberType.CHILD), notification);
            }
        }
    }

    @Transactional
    public void createToSystem(String title, String content, Notification notification, CommonMember member) throws Exception {

        MemberAlarm memberAlarm = MemberAlarm.builder()
                .commonMember(member)
                .title(title)
                .content(content)
                .isRead(false)
                .alarmId(0L)
                .alarmType(AlarmType.SYSTEM)
                .build();

        String pushToken = member.getPushToken();
        if (!pushToken.isEmpty() && member.getAgreeToService()) {
            pushMessageManagement.send(pushToken, notification);
        }

        memberAlarmRepository.save(memberAlarm);
    }

    @Transactional(readOnly = true)
    public Page<MemberAlarmDto> get(Long commonMemberId, Pageable pageable) throws Exception {
        return memberAlarmRepository.search(pageable, commonMemberId);
    }

    @Transactional()
    public MemberAlarmDto getById(Long memberAlarmId) throws Exception {

        Optional<MemberAlarm> memberAlarmOptional = memberAlarmRepository.findById(memberAlarmId);

        if (memberAlarmOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        MemberAlarm memberAlarm = memberAlarmOptional.get();
        memberAlarm.read();
        memberAlarmRepository.save(memberAlarm);

        return MemberAlarmDto.create(memberAlarm);
    }

    @Transactional()
    public Boolean getNewExist(Long commonMemberId) throws Exception {

        int newAlarmCount = memberAlarmRepository.countByCommonMemberIdAndIsRead(commonMemberId, false);

        return newAlarmCount > 0;
    }
    
    @Transactional
    public void createToSystemByMemberId(String title, String content, Notification notification, Long memberId) throws Exception {
        Optional<CommonMember> memberOptional = commonMemberRepository.findById(memberId);
        
        if (memberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        
        CommonMember member = memberOptional.get();
        createToSystem(title, content, notification, member);
    }
}
