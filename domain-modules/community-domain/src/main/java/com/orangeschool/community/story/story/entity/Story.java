package com.orangeschool.community.story.story.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.story.comment.entity.StoryComment;
import com.orangeschool.community.story.like.entity.StoryLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Story extends BaseEntity {

    private Long memberId;

    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    // 지역 태그
    private String regionTag;

    // 이미지 모음
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryImage> images = new HashSet<>();

    // 댓글 모음
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryComment> storyComments = new HashSet<>();

    // 좋아요 모음
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryLike> storyLikes = new HashSet<>();

    // 부모 타입
    @Enumerated(EnumType.STRING)
    private com.orangeschool.common.enums.MemberType memberType;

    // 활성화 여부
    @Column(columnDefinition = "boolean default false")
    private boolean activation;

    // 차단 여부
    @Column(columnDefinition = "boolean default false")
    private boolean isBlock;

    // 수정
    public void update(String title, String content, String regionTag) {
        this.title = title;
        this.content = content;
        this.regionTag = regionTag;
    }

    public void addImages(Set<StoryImage> images) {
        this.images.addAll(images);
        images.forEach(image -> image.setStory(this));
    }

    public void removeImage(StoryImage image) {
        this.images.remove(image);
        image.setStory(null);
    }

    public void updateActivation(boolean activation) {
        this.activation = activation;
    }
}
