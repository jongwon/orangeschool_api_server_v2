package com.orangeschool.community.pick.pick.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.PickType;
import com.orangeschool.community.pick.comment.entity.PickComment;
import com.orangeschool.community.pick.like.entity.PickLike;
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
public class Pick extends BaseEntity {

    private Long number;
    private PickType pickType;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private String link;
    @Column(columnDefinition = "TEXT")
    private String originFileName;
    @Column(columnDefinition = "TEXT")
    private String serverFileName;
    @Column(columnDefinition = "TEXT")
    private String fileUrl;
    private String writerEmail;

    private String previewContent;
    private String linkBtnName;

    private Boolean isActive;
    private Long viewCount;
    private int totalCommentCount;

    @OneToMany(mappedBy = "pick", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    @Builder.Default
    private Set<PickImage> images = new HashSet<>();

    @OneToMany(mappedBy = "pick", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    @Builder.Default
    private Set<PickComment> pickComments = new HashSet<>();

    @OneToMany(mappedBy = "pick", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    @Builder.Default
    private Set<PickLike> pickLikes = new HashSet<>();

    public void setFile(String originFileName, String serverFileName, String fileUrl) {
        this.originFileName = originFileName;
        this.serverFileName = serverFileName;
        this.fileUrl = fileUrl;
    }

    public void deleteFile() {
        this.serverFileName = "";
        this.originFileName = "";
        this.fileUrl = "";
    }

    public void update(Long number, PickType pickType, String title, String content, String link, String previewContent, String linkBtnName) {
        this.number = number;
        this.pickType = pickType;
        this.title = title;
        this.content = content;
        this.link = link;
        this.previewContent = previewContent;
        this.linkBtnName = linkBtnName;
    }

    public void updateViewCount() {
        this.viewCount += 1;
    }

    public void updateActivation(Boolean activation) {
        this.isActive = activation;
    }

    public void updateTotalCommentCount(int totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }
}
