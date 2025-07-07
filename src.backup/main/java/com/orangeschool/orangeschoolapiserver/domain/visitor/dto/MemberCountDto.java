package com.orangeschool.orangeschoolapiserver.domain.visitor.dto;

import lombok.Data;

@Data
public class MemberCountDto {

    private Long totalCount;
    private Long joinCount;
    private Long leaveCount;

    private Long parentCount; // 활성 보호자 수
    private Long parentJoinCount; // 보호자 가입 수
    private Long parentLeaveCount; // 보호자 탈퇴 수

    private Long childCount;  // 활성 자녀 수
    private Long childJoinCount;  // 활성 자녀 수
    private Long childLeaveCount;  // 활성 자녀 수
}
