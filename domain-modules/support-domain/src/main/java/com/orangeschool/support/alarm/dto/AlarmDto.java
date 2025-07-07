package com.orangeschool.support.alarm.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.enums.AlarmMemberType;
import com.orangeschool.common.enums.AlarmType;
import com.orangeschool.support.alarm.entity.Alarm;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmDto extends CommonDto {
    
    private String title;
    private String content;
    private AlarmType alarmType;
    private String alarmTypeTitle;
    private AlarmMemberType alarmMemberType;
    private String alarmMemberTypeTitle;

    public static AlarmDto create(Alarm alarm) {

        AlarmDto alarmDto = AlarmDto.builder()
                .title(alarm.getTitle())
                .content(alarm.getContent())
                .alarmType(alarm.getAlarmType())
                .alarmTypeTitle(alarm.getAlarmType().getTitle())
                .alarmMemberType(alarm.getAlarmMemberType())
                .alarmMemberTypeTitle(alarm.getAlarmMemberType().getTitle())
                .build();

        alarmDto.setCreatedAt(alarm.getCreatedAt());
        alarmDto.setUpdatedAt(alarm.getUpdatedAt());
        alarmDto.setId(alarm.getId());

        return alarmDto;
    }


}
