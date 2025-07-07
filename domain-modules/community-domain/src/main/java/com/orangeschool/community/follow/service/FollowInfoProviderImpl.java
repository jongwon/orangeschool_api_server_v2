package com.orangeschool.community.follow.service;

import com.orangeschool.community.api.service.FollowInfoProvider;
import com.orangeschool.community.api.dto.FollowInfo;
import com.orangeschool.community.follow.entity.Follow;
import com.orangeschool.community.follow.repository.FollowRepository;
import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.member.api.dto.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FollowInfoProviderImpl implements FollowInfoProvider {

    private final FollowRepository followRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Override
    public Long countFollowers(Long memberId) {
        return followRepository.countByFollowerMemberId(memberId);
    }

    @Override
    public Long countFollowing(Long memberId) {
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
                .map(follow -> {
                    Optional<MemberInfo> followingMember = memberInfoProvider.getMemberInfo(follow.getFollowingMemberId());
                    Optional<MemberInfo> followerMember = memberInfoProvider.getMemberInfo(follow.getFollowerMemberId());
                    
                    return FollowInfo.builder()
                            .id(follow.getId())
                            .followingMemberId(follow.getFollowingMemberId())
                            .followingMemberNickname(followingMember.map(MemberInfo::getNickname).orElse(""))
                            .followingMemberProfileImage(followingMember.map(MemberInfo::getProfileImage).orElse(""))
                            .followerMemberId(follow.getFollowerMemberId())
                            .followerMemberNickname(followerMember.map(MemberInfo::getNickname).orElse(""))
                            .followerMemberProfileImage(followerMember.map(MemberInfo::getProfileImage).orElse(""))
                            .createdAt(follow.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FollowInfo> getFollowings(Long memberId) {
        List<Follow> followings = followRepository.findByFollowingMemberId(memberId);
        
        return followings.stream()
                .map(follow -> {
                    Optional<MemberInfo> followingMember = memberInfoProvider.getMemberInfo(follow.getFollowingMemberId());
                    Optional<MemberInfo> followerMember = memberInfoProvider.getMemberInfo(follow.getFollowerMemberId());
                    
                    return FollowInfo.builder()
                            .id(follow.getId())
                            .followingMemberId(follow.getFollowingMemberId())
                            .followingMemberNickname(followingMember.map(MemberInfo::getNickname).orElse(""))
                            .followingMemberProfileImage(followingMember.map(MemberInfo::getProfileImage).orElse(""))
                            .followerMemberId(follow.getFollowerMemberId())
                            .followerMemberNickname(followerMember.map(MemberInfo::getNickname).orElse(""))
                            .followerMemberProfileImage(followerMember.map(MemberInfo::getProfileImage).orElse(""))
                            .createdAt(follow.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public <T> Page<T> searchFollowers(Long followingMemberId, Pageable pageable) {
        // TODO: 추후 구현
        return Page.empty();
    }
}