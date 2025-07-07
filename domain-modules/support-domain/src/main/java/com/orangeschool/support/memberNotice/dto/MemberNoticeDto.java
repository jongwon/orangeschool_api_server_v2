package com.orangeschool.support.memberNotice.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.support.memberNotice.entity.MemberNotice;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberNoticeDto extends CommonDto {

    private String title;
    private String content;
    private Boolean isRead;

    public static MemberNoticeDto create(MemberNotice memberNotice) {

        MemberNoticeDto memberNoticeDto = MemberNoticeDto.builder()
                .title(memberNotice.getTitle())
                .content(memberNotice.getContent())
                .isRead(memberNotice.getIsRead())
                .build();

        memberNoticeDto.setCreatedAt(memberNotice.getCreatedAt());
        memberNoticeDto.setUpdatedAt(memberNotice.getUpdatedAt());
        memberNoticeDto.setId(memberNotice.getId());

        return memberNoticeDto;
    }

}
