package com.orangeschool.community.story.comment.dto;

import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.community.story.reply.dto.StoryReplyDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class StoryCommentDto {
    private Long id;
    private Long storyId;
    private MemberInfo member;
    private String content;
    private LocalDateTime createdAt;
    private List<StoryReplyDto> replies;
}
