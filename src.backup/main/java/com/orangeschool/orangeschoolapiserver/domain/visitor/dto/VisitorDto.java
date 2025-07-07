package com.orangeschool.orangeschoolapiserver.domain.visitor.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.visitor.entity.Visitor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VisitorDto extends CommonDto {
    private LocalDate today;
    private Integer count;

    public static VisitorDto create(Visitor visitor) {

        VisitorDto visitorDto = VisitorDto.builder()
                .today(visitor.getToday())
                .count(visitor.getCount())
                .build();

        visitorDto.setCreatedAt(visitor.getCreatedAt());
        visitorDto.setUpdatedAt(visitor.getUpdatedAt());
        visitorDto.setId(visitor.getId());

        return visitorDto;
    }
}
