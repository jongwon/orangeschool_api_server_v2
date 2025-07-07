package com.orangeschool.community.follow;

import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.api.MemberInfoProvider;
import com.orangeschool.community.follow.entity.Follow;
import com.orangeschool.community.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public void follow(Long followingMemberId, Long followerMemberId) throws Exception {

        if (followingMemberId == followerMemberId) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        Optional<CommonMember> followingMemberOptional = memberInfoProvider.getMemberInfo(followingMemberId);

        if (followingMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<CommonMember> followerMemberOptional = memberInfoProvider.getMemberInfo(followerMemberId);

        if (followerMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<Follow> followOptional = followRepository
                .findByFollowingMemberIdAndFollowerMemberId(followingMemberId, followerMemberId);

        if (followOptional.isPresent()) {
            followRepository.deleteById(followOptional.get().getId());
        } else {
            Follow follow = Follow.builder()
                    .followingMember(followingMemberOptional.get())
                    .followerMember(followerMemberOptional.get())
                    .build();

            followRepository.save(follow);
        }
    }
}
