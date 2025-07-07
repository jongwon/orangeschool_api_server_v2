package com.orangeschool.support.suggest.dto;

import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.member.commonMember.dto.CommonMemberDto;
import com.orangeschool.support.suggest.entity.Suggest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuggestDto extends CommonDto {

    private CommonMemberDto commonMember;

    private String title;
    private String content;

    public static SuggestDto create(Suggest suggest) {

        SuggestDto reportDto = SuggestDto.builder()
                .commonMember(CommonMemberDto.create(suggest.getCommonMember()))
                .title(suggest.getTitle())
                .content(suggest.getContent())
                .build();

        reportDto.setCreatedAt(suggest.getCreatedAt());
        reportDto.setUpdatedAt(suggest.getUpdatedAt());
        reportDto.setId(suggest.getId());

        return reportDto;
    }
}
