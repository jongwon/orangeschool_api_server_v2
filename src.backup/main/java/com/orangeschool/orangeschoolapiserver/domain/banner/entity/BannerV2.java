package com.orangeschool.orangeschoolapiserver.domain.banner.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BannerV2 extends CommonEntity {

    private String title;
    private String regionCodeTag; // 예: "#1168000000#1174000000#1130500000"
    private String regionNameTag; // 예: "#1168000000#1174000000#1130500000"
    @Column(columnDefinition = "TEXT")
    private String originFileName;
    @Column(columnDefinition = "TEXT")
    private String serverFileName;
    @Column(columnDefinition = "TEXT")
    private String fileUrl;
    private String link;

    private Boolean isActive;
    private Long viewCount;

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

    public void update(String title, String regionCodeTag, String regionNameTag, String link, Boolean isActive) {
        this.title = title;
        this.regionCodeTag = regionCodeTag;
        this.regionNameTag = regionNameTag;
        this.link = link;
        this.isActive = isActive;
    }

    public void updateActivation(Boolean activation) {
        this.isActive = activation;
    }

    public void updateViewCount() {
        this.viewCount += 1;
    }
}