package com.orangeschool.community.pick.comment.dto;

import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.community.pick.reply.dto.PickReplyDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class PickCommentDto {
    private Long id;
    private Long pickId;
    private MemberInfo member;
    private String content;
    private LocalDateTime createdAt;
    private List<PickReplyDto> replies;
}
