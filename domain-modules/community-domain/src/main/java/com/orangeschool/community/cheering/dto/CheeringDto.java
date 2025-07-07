package com.orangeschool.community.cheering.dto;

import com.orangeschool.common.enums.CheeringMessage;
import com.orangeschool.member.api.dto.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;import lombok.Builder;
import lombok.EqualsAndHashCode;import lombok.Data;
import lombok.EqualsAndHashCode;import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheeringDto {
    private Long id;
    private MemberInfo cheeringMember;
    private MemberInfo cheeredMember;
    private CheeringMessage message;
    private LocalDateTime createdAt;
}
