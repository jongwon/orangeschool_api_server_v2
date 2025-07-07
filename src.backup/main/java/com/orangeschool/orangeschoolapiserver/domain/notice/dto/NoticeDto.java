package com.orangeschool.orangeschoolapiserver.domain.notice.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.notice.entity.Notice;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeDto extends CommonDto {
    
    private String title;
    private String content;
    private String writerEmail;

    public static NoticeDto create(Notice notice) {

        NoticeDto noticeDto = NoticeDto.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .writerEmail(notice.getWriterEmail())
                .build();

        noticeDto.setCreatedAt(notice.getCreatedAt());
        noticeDto.setUpdatedAt(notice.getUpdatedAt());
        noticeDto.setId(notice.getId());

        return noticeDto;
    }

}
