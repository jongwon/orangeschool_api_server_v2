package com.orangeschool.community.story.reply.dto;

import com.orangeschool.member.api.dto.MemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class StoryReplyDto {
    private Long id;
    private Long storyCommentId;
    private MemberInfo member;
    private String content;
    private LocalDateTime createdAt;
}
