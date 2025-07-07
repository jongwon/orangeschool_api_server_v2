package com.orangeschool.community.api.service;

import com.orangeschool.community.api.dto.FollowInfo;

import java.util.List;

/**
 * 팔로우 정보를 제공하는 인터페이스
 * 다른 도메인에서 팔로우 관련 정보가 필요할 때 사용
 */
public interface FollowInfoProvider {
    
    /**
     * 특정 회원의 팔로워 수 조회
     *
     * @param memberId 회원 ID
     * @return 팔로워 수
     */
    Long getFollowerCount(Long memberId);
    
    /**
     * 특정 회원의 팔로잉 수 조회
     *
     * @param memberId 회원 ID
     * @return 팔로잉 수
     */
    Long getFollowingCount(Long memberId);
    
    /**
     * 두 회원 간 팔로우 여부 확인
     *
     * @param followingMemberId 팔로우하는 회원 ID
     * @param followerMemberId 팔로우받는 회원 ID
     * @return 팔로우 여부
     */
    boolean isFollowing(Long followingMemberId, Long followerMemberId);
    
    /**
     * 특정 회원의 팔로워 목록 조회
     *
     * @param memberId 회원 ID
     * @return 팔로워 정보 목록
     */
    List<FollowInfo> getFollowers(Long memberId);
    
    /**
     * 특정 회원의 팔로잉 목록 조회
     *
     * @param memberId 회원 ID
     * @return 팔로잉 정보 목록
     */
    List<FollowInfo> getFollowings(Long memberId);
    
    /**
     * 특정 회원의 팔로워 수 조회 (별칭)
     */
    Long countFollowers(Long memberId);
    
    /**
     * 특정 회원의 팔로잉 수 조회 (별칭)
     */
    Long countFollowing(Long memberId);
    
    /**
     * 특정 회원의 팔로워 검색
     * @param memberId 회원 ID
     * @param pageable 페이징 정보
     * @return 팔로워 정보 페이지
     */
    <T> T searchFollowers(Long memberId, org.springframework.data.domain.Pageable pageable);
}