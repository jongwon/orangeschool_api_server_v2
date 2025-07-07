package com.orangeschool.orangeschoolapiserver.domain.popup.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Popup extends CommonEntity {

    private String title;
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

    public void update(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public void updateActivation(Boolean activation) {
        this.isActive = activation;
    }

    public void updateViewCount() {
        this.viewCount += 1;
    }
}
