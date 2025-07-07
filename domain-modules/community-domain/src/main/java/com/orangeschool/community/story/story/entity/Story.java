package com.orangeschool.community.story.story.entity;

import com.orangeschool.common.entity.CommonEntity;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.story.comment.entity.StoryComment;
import com.orangeschool.community.story.like.entity.StoryLike;
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
public class Story extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;

    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    // 지역 태그
    private String regionNameTag; // 예: "#서울 강남구#강원 강릉시#서울 강북구", 시도 시군구 title 배열
    private String regionCodeTag; // 예: "#1168000000#1174000000#1130500000", 시군구 value 배열

    private boolean popular;
    private Boolean isActive;
    private Long viewCount;
    private Long todayViewCount;
    private int totalCommentCount;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<StoryImage> images = new HashSet<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private Set<StoryComment> storyComments = new HashSet<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private Set<StoryLike> storyLikes = new HashSet<>();

    public void update(String title, String content, String regionNameTag, String regionCodeTag) {
        this.title = title;
        this.content = content;
        this.regionNameTag = regionNameTag;
        this.regionCodeTag = regionCodeTag;
    }

    public void updateViewCount() {
        this.viewCount += 1;
        this.todayViewCount += 1;
    }

    public void updateActivation(Boolean activation) {
        this.isActive = activation;
    }

    public void resetTodayViewCount() {
        this.todayViewCount = 0L;
    }

    public void updateTotalCommentCount(int totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }
}
