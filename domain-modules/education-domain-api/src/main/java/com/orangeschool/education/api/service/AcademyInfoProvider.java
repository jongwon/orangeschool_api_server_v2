package com.orangeschool.education.api.service;

import com.orangeschool.education.api.dto.AcademyInfo;
import com.orangeschool.education.api.dto.MemberAcademyInfo;
import java.util.List;
import java.util.Optional;

/**
 * 학원 정보 제공 인터페이스
 */
public interface AcademyInfoProvider {
    
    /**
     * 학원 정보 조회
     * @param academyId 학원 ID
     * @return 학원 정보
     */
    Optional<AcademyInfo> getAcademyInfo(Long academyId);
    
    /**
     * 학원 존재 여부 확인
     * @param academyId 학원 ID
     * @return 존재 여부
     */
    boolean existsAcademy(Long academyId);
    
    /**
     * 회원이 속한 학원 목록 조회
     * @param memberId 회원 ID
     * @return 학원 목록
     */
    List<AcademyInfo> getMemberAcademies(Long memberId);
    
    /**
     * 회원-학원 관계 정보 조회
     * @param memberId 회원 ID
     * @param academyId 학원 ID
     * @return 회원-학원 관계 정보
     */
    Optional<MemberAcademyInfo> getMemberAcademyInfo(Long memberId, Long academyId);
    
    /**
     * 학원에 속한 회원 ID 목록 조회
     * @param academyId 학원 ID
     * @return 회원 ID 목록
     */
    List<Long> getAcademyMemberIds(Long academyId);
}