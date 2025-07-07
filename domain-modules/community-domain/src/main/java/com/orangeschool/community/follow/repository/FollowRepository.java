package com.orangeschool.community.follow.repository;

import com.orangeschool.community.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom{

    Optional<Follow> findByFollowingMemberIdAndFollowerMemberId(Long followingMemberId, Long followerMemberId);
    Long countByFollowingMemberId(Long followingMemberId);
    Long countByFollowerMemberId(Long followerMemberId);
    boolean existsByFollowingMemberIdAndFollowerMemberId(Long followingMemberId, Long followerMemberId);
    List<Follow> findByFollowerMemberId(Long followerMemberId);
    List<Follow> findByFollowingMemberId(Long followingMemberId);
}
