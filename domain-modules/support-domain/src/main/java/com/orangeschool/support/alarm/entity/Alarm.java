package com.orangeschool.support.alarm.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.AlarmMemberType;
import com.orangeschool.common.enums.AlarmType;
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
public class Alarm extends BaseEntity {

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private AlarmType alarmType;
    private AlarmMemberType alarmMemberType;
}
