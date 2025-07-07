package com.orangeschool.community.story.comment.entity;

import com.orangeschool.common.entity.CommonEntity;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.story.reply.entity.StoryReply;
import com.orangeschool.community.story.story.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StoryComment extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyId")
    private Story story;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @OneToMany(mappedBy = "storyComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<StoryReply> storyReplies = new HashSet<>();

    public void update(String content) {
        this.content = content;
    }
}
