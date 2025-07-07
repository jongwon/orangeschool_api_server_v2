package com.orangeschool.support.banner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.location.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Banner extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId")
    private Location location;

    @Column(columnDefinition = "TEXT")
    private String originFileName;
    @Column(columnDefinition = "TEXT")
    private String serverFileName;
    @Column(columnDefinition = "TEXT")
    private String fileUrl;
    private String link;

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

    public void update(String link) {
        this.link = link;
    }
}