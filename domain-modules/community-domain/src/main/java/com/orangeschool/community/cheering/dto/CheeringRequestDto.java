package com.orangeschool.community.cheering.dto;

import com.orangeschool.common.enums.CheeringMessage;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CheeringRequestDto {
    private Long cheeringMemberId;
    private Long cheeredMemberId;
    private CheeringMessage message;
}
