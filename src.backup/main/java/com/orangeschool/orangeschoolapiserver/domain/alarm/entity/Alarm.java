package com.orangeschool.orangeschoolapiserver.domain.alarm.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.AlarmMemberType;
import com.orangeschool.orangeschoolapiserver.common.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Alarm extends CommonEntity {

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private AlarmType alarmType;
    private AlarmMemberType alarmMemberType;
}
