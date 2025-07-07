package com.orangeschool.community.follow.repository;

import com.orangeschool.community.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {
    
    Optional<Follow> findByFollowingMemberIdAndFollowerMemberId(Long followingMemberId, Long followerMemberId);
    
    boolean existsByFollowingMemberIdAndFollowerMemberId(Long followingMemberId, Long followerMemberId);
    
    Long countByFollowerMemberId(Long followerMemberId);
    
    Long countByFollowingMemberId(Long followingMemberId);
}
