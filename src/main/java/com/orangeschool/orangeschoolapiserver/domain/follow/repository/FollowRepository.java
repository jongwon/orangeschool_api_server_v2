package com.orangeschool.orangeschoolapiserver.domain.follow.repository;

import com.orangeschool.orangeschoolapiserver.domain.follow.entity.Follow;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface FollowRepository extends PagingAndSortingRepository<Follow, Long>, FollowRepositoryCustom{

    Optional<Follow> findByFollowingMemberIdAndFollowerMemberId(Long followingMemberId, Long followerMemberId);
    int countByFollowingMemberId(Long followingMemberId);
    int countByFollowerMemberId(Long followerMemberId);
}
