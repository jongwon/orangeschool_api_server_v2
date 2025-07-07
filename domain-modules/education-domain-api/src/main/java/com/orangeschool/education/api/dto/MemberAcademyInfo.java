package com.orangeschool.education.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원-학원 관계 정보 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAcademyInfo {
    private Long id;
    private Long memberId;
    private Long academyId;
    private String memberName;
    private String academyName;
    private boolean isActive;
    private LocalDateTime joinedAt;
}