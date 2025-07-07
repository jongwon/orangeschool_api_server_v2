package com.orangeschool.community.follow.service;

import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.community.follow.entity.Follow;
import com.orangeschool.community.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public void follow(Long followingMemberId, Long followerMemberId) {
        // 자기 자신을 팔로우할 수 없음
        if (followingMemberId.equals(followerMemberId)) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }
        
        // 회원 존재 여부 확인
        Optional<MemberInfo> followingMember = memberInfoProvider.getMemberInfo(followingMemberId);
        Optional<MemberInfo> followerMember = memberInfoProvider.getMemberInfo(followerMemberId);

        if (followingMember.isEmpty() || followerMember.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_USER);
        }

        // 이미 팔로우 중인지 확인
        Optional<Follow> existingFollow = followRepository.findByFollowingMemberIdAndFollowerMemberId(
            followingMemberId, followerMemberId
        );

        if (existingFollow.isPresent()) {
            throw new CustomException(ResponseCode.ALREADY_FOLLOWING);
        }

        Follow follow = Follow.builder()
            .followingMemberId(followingMemberId)
            .followerMemberId(followerMemberId)
            .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followingMemberId, Long followerMemberId) {
        Follow follow = followRepository.findByFollowingMemberIdAndFollowerMemberId(
            followingMemberId, followerMemberId
        ).orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_FOLLOW));

        followRepository.delete(follow);
    }

    public boolean isFollowing(Long followingMemberId, Long followerMemberId) {
        return followRepository.existsByFollowingMemberIdAndFollowerMemberId(
            followingMemberId, followerMemberId
        );
    }

    public Long countFollowers(Long memberId) {
        return followRepository.countByFollowerMemberId(memberId);
    }

    public Long countFollowing(Long memberId) {
        return followRepository.countByFollowingMemberId(memberId);
    }
}
