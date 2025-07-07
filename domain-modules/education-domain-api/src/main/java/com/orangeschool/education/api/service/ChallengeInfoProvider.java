package com.orangeschool.education.api.service;

public interface ChallengeInfoProvider {
    boolean hasChallenges(Long memberId);
    
    /**
     * 진행 중인 챌린지 정보 조회
     * @param memberId 회원 ID
     * @return 챌린지 정보 (없으면 null)
     */
    Object getProgressChallengeByMemberId(Long memberId);
}