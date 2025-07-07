package com.orangeschool.orangeschoolapiserver.domain.cheering.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.CheeringMessage;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cheering extends CommonEntity {

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
