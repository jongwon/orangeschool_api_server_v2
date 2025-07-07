package com.orangeschool.community.cheering.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.CheeringMessage;

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
public class Cheering extends BaseEntity {

    // 응원하는사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cheeringMemberId")
    private CommonMember cheeringMember;

    // 응원받는사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cheeredMemberId")
    private CommonMember cheeredMember;

    private CheeringMessage cheeringMessage;
}
