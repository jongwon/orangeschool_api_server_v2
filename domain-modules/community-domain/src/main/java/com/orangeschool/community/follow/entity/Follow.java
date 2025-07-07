package com.orangeschool.community.follow.entity;

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
public class Follow extends BaseEntity {

    // 팔로우 하려는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followingMemberId")
    private CommonMember followingMember;

    // 팔로우 당하는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followerMemberId")
    private CommonMember followerMember;
}
