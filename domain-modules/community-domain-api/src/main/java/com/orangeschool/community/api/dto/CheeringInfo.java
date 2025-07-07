package com.orangeschool.community.api.dto;

import com.orangeschool.common.enums.CheeringMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheeringInfo {
    private Long id;
    private Long cheeringMemberId;
    private String cheeringMemberNickname;
    private String cheeringMemberProfileImage;
    private Long cheeredMemberId;
    private String cheeredMemberNickname;
    private CheeringMessage cheeringMessage;
    private LocalDateTime createdAt;
}