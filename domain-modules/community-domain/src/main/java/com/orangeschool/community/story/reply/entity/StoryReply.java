package com.orangeschool.community.story.reply.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.story.comment.entity.StoryComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StoryReply extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyCommentId")
    private StoryComment storyComment;

    private String content;
}
