package com.orangeschool.community.story.comment.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.story.story.entity.Story;
import com.orangeschool.community.story.reply.entity.StoryReply;
import lombok.*;
import lombok.Builder;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StoryComment extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyId")
    private Story story;

    private String content;

    @OneToMany(mappedBy = "storyComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryReply> storyReplies = new HashSet<>();
}
