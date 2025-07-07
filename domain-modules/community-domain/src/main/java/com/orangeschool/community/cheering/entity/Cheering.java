package com.orangeschool.community.cheering.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.CheeringMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cheering extends BaseEntity {

    // 응원을 보내는 사람
    private Long cheeringMemberId;

    // 응원을 받는 사람
    private Long cheeredMemberId;

    @Enumerated(EnumType.STRING)
    private CheeringMessage message;
}
