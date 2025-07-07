package com.orangeschool.orangeschoolapiserver.domain.challengeTemp.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.ConfirmStatus;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChallengeTemp extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;
    @Column(columnDefinition = "TEXT")
    private String mission;
    private int requiredOrangeCount;
    @Column(columnDefinition = "TEXT")
    private String reward;
    private Boolean isShow;

    // 확인상태
    private ConfirmStatus confirmStatus;
    private Long challengeId;

    public void update(String mission, int requiredOrangeCount, String reward, Boolean isShow) {
        this.mission = mission;
        this.requiredOrangeCount = requiredOrangeCount;
        this.reward = reward;
        this.isShow = isShow;
    }
}
