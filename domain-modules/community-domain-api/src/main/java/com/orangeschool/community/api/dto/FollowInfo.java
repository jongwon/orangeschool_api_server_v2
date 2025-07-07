package com.orangeschool.community.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowInfo {
    private Long id;
    private Long followingMemberId;
    private String followingMemberNickname;
    private String followingMemberProfileImage;
    private Long followerMemberId;
    private String followerMemberNickname;
    private String followerMemberProfileImage;
    private LocalDateTime createdAt;
}