package com.orangeschool.member.api.dto;

import com.orangeschool.common.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원 기본 정보 DTO
 * 다른 도메인에서 필요한 최소한의 회원 정보만 포함
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfo {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private MemberType memberType;
    private String profileImage;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    // 학생인 경우 추가 정보
    private String schoolName;
    private String grade;
    private Long parentId;
    
    // 부모인 경우 추가 정보
    private String phoneNumber;
}