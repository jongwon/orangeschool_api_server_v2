package com.orangeschool.education.schoolSchedule.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.member.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SchoolSchedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;

    private String keyStringValue;
    private Boolean isImportant;
    private String color;

    public void update(Boolean isImportant, String color) {
        this.isImportant = isImportant;
        this.color = color;
    }
}
