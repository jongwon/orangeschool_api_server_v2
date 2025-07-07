package com.orangeschool.orangeschoolapiserver.domain.follow;

import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.follow.entity.Follow;
import com.orangeschool.orangeschoolapiserver.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final CommonMemberRepository commonMemberRepository;

    @Transactional
    public void follow(Long followingMemberId, Long followerMemberId) throws Exception {

        if (followingMemberId == followerMemberId) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        Optional<CommonMember> followingMemberOptional = commonMemberRepository.findById(followingMemberId);

        if (followingMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<CommonMember> followerMemberOptional = commonMemberRepository.findById(followerMemberId);

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
