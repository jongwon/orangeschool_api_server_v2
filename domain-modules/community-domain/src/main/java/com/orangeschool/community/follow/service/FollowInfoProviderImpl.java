package com.orangeschool.community.follow.service;

import com.orangeschool.community.api.dto.FollowInfo;
import com.orangeschool.community.api.service.FollowInfoProvider;
import com.orangeschool.community.follow.entity.Follow;
import com.orangeschool.community.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FollowInfoProviderImpl implements FollowInfoProvider {

    private final FollowRepository followRepository;

    @Override
    public Long getFollowerCount(Long memberId) {
        return followRepository.countByFollowerMemberId(memberId);
    }

    @Override
    public Long getFollowingCount(Long memberId) {
        return followRepository.countByFollowingMemberId(memberId);
    }

    @Override
    public boolean isFollowing(Long followingMemberId, Long followerMemberId) {
        return followRepository.existsByFollowingMemberIdAndFollowerMemberId(followingMemberId, followerMemberId);
    }

    @Override
    public List<FollowInfo> getFollowers(Long memberId) {
        List<Follow> followers = followRepository.findByFollowerMemberId(memberId);
        
        return followers.stream()
                .map(follow -> FollowInfo.builder()
                        .id(follow.getId())
                        .followingMemberId(follow.getFollowingMember().getId())
                        .followingMemberNickname(follow.getFollowingMember().getNickname())
                        .followingMemberProfileImage(follow.getFollowingMember().getProfileImage())
                        .followerMemberId(follow.getFollowerMember().getId())
                        .followerMemberNickname(follow.getFollowerMember().getNickname())
                        .followerMemberProfileImage(follow.getFollowerMember().getProfileImage())
                        .createdAt(follow.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<FollowInfo> getFollowings(Long memberId) {
        List<Follow> followings = followRepository.findByFollowingMemberId(memberId);
        
        return followings.stream()
                .map(follow -> FollowInfo.builder()
                        .id(follow.getId())
                        .followingMemberId(follow.getFollowingMember().getId())
                        .followingMemberNickname(follow.getFollowingMember().getNickname())
                        .followingMemberProfileImage(follow.getFollowingMember().getProfileImage())
                        .followerMemberId(follow.getFollowerMember().getId())
                        .followerMemberNickname(follow.getFollowerMember().getNickname())
                        .followerMemberProfileImage(follow.getFollowerMember().getProfileImage())
                        .createdAt(follow.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}