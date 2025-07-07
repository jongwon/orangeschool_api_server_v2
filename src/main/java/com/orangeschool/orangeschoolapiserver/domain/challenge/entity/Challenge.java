package com.orangeschool.orangeschoolapiserver.domain.challenge.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.ChallengeStatus;
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
public class Challenge extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;
    private ChallengeStatus challengeStatus;
    @Column(columnDefinition = "TEXT")
    private String mission;
    private int currentStampCount;
    private int currentOrangeCount;
    private int requiredOrangeCount;
    @Column(columnDefinition = "TEXT")
    private String reward;
    private Boolean isShow;

    public void update(String mission, Boolean isShow, String reward, int requiredOrangeCount) {
        this.mission = mission;
        this.isShow = isShow;
        this.reward = reward;
        this.requiredOrangeCount = requiredOrangeCount;
    }

    public void updateCurrentStampCount(int currentStampCount) {
        this.currentStampCount = currentStampCount;
    }

    public void updateCurrentOrangeCount(int currentOrangeCount) {
        this.currentOrangeCount = currentOrangeCount;
    }

    public void updateChallengeStatus(ChallengeStatus challengeStatus) {
        this.challengeStatus = challengeStatus;
    }

    public void updateShowFlag(Boolean showFlag) {
        this.isShow = showFlag;
    }
}
