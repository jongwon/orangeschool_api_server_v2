package com.orangeschool.education.memberAcademy.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.education.academy.entity.Academy;
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
public class MemberAcademy extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academyId")
    private Academy academy;
}
