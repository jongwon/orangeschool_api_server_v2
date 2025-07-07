package com.orangeschool.community.follow.entity;

import com.orangeschool.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Follow extends BaseEntity {

    // 팔로우 하려는 사람
    private Long followingMemberId;

    // 팔로우 당하는 사용자
    private Long followerMemberId;
}
