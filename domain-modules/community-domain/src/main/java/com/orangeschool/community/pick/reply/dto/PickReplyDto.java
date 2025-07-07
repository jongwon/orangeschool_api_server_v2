package com.orangeschool.community.pick.reply.dto;

import com.orangeschool.member.api.dto.MemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class PickReplyDto {
    private Long id;
    private Long pickCommentId;
    private MemberInfo member;
    private String content;
    private LocalDateTime createdAt;
}
