package com.orangeschool.orangeschoolapiserver.domain.terms.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.entity.Terms;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TermsDto extends CommonDto {

    private String title;
    private String content;

    public static TermsDto create(Terms terms) {

        TermsDto termsDto = TermsDto.builder()
                .title(terms.getTitle())
                .content(terms.getContent())
                .build();

        termsDto.setCreatedAt(terms.getCreatedAt());
        termsDto.setUpdatedAt(terms.getUpdatedAt());
        termsDto.setId(terms.getId());

        return termsDto;
    }
}
