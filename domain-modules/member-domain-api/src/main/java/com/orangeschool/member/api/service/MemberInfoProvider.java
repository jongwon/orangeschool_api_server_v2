package com.orangeschool.member.api.service;

import com.orangeschool.member.api.dto.MemberInfo;
import java.util.List;
import java.util.Optional;

/**
 * 회원 정보 제공 인터페이스
 * 다른 도메인에서 회원 정보가 필요할 때 사용
 */
public interface MemberInfoProvider {
    
    /**
     * 회원 정보 조회
     * @param memberId 회원 ID
     * @return 회원 정보
     */
    Optional<MemberInfo> getMemberInfo(Long memberId);
    
    /**
     * 회원 존재 여부 확인
     * @param memberId 회원 ID
     * @return 존재 여부
     */
    boolean existsMember(Long memberId);
    
    /**
     * 여러 회원 정보 조회
     * @param memberIds 회원 ID 목록
     * @return 회원 정보 목록
     */
    List<MemberInfo> getMembersByIds(List<Long> memberIds);
    
    /**
     * 이메일로 회원 조회
     * @param email 이메일
     * @return 회원 정보
     */
    Optional<MemberInfo> getMemberByEmail(String email);
    
    /**
     * 회원 활성화 상태 확인
     * @param memberId 회원 ID
     * @return 활성화 상태
     */
    boolean isActiveMember(Long memberId);
}