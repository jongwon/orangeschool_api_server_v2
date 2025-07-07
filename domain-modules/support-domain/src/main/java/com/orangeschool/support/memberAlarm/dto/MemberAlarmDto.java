package com.orangeschool.support.memberAlarm.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.enums.AlarmType;
import com.orangeschool.support.memberAlarm.entity.MemberAlarm;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberAlarmDto extends CommonDto {

    private Long commonMemberId;
    private String title;
    private String content;
    private Boolean isRead;
    private AlarmType alarmType;
    private String alarmTypeTitle;
    private Long alarmId;

    public static MemberAlarmDto create(MemberAlarm memberAlarm) {

        MemberAlarmDto memberAlarmDto = MemberAlarmDto.builder()
                .commonMemberId(memberAlarm.getCommonMember().getId())
                .title(memberAlarm.getTitle())
                .content(memberAlarm.getContent())
                .isRead(memberAlarm.getIsRead())
                .alarmType(memberAlarm.getAlarmType())
                .alarmTypeTitle(memberAlarm.getAlarmType().getTitle())
                .alarmId(memberAlarm.getAlarmId())
                .build();

        memberAlarmDto.setCreatedAt(memberAlarm.getCreatedAt());
        memberAlarmDto.setUpdatedAt(memberAlarm.getUpdatedAt());
        memberAlarmDto.setId(memberAlarm.getId());

        return memberAlarmDto;
    }

}
