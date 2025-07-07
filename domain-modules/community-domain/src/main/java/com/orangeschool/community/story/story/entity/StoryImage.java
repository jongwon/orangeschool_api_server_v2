package com.orangeschool.community.story.story.entity;

import com.orangeschool.common.entity.BaseEntity;
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
public class StoryImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyId")
    private Story story;
    @Column(columnDefinition = "TEXT")
    private String serverFileName;
    @Column(columnDefinition = "TEXT")
    private String originFileName;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
}
