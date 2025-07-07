package com.orangeschool.orangeschoolapiserver.domain.memberAlarm.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.common.enums.AlarmType;
import com.orangeschool.orangeschoolapiserver.domain.memberAlarm.entity.MemberAlarm;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberAlarmDto extends CommonDto {

    private String title;
    private String content;
    private Boolean isRead;
    private AlarmType alarmType;
    private String alarmTypeTitle;

    public static MemberAlarmDto create(MemberAlarm memberAlarm) {

        MemberAlarmDto memberAlarmDto = MemberAlarmDto.builder()
                .title(memberAlarm.getTitle())
                .content(memberAlarm.getContent())
                .isRead(memberAlarm.getIsRead())
                .alarmType(memberAlarm.getAlarmType())
                .alarmTypeTitle(memberAlarm.getAlarmType().getTitle())
                .build();

        memberAlarmDto.setCreatedAt(memberAlarm.getCreatedAt());
        memberAlarmDto.setUpdatedAt(memberAlarm.getUpdatedAt());
        memberAlarmDto.setId(memberAlarm.getId());

        return memberAlarmDto;
    }

}
