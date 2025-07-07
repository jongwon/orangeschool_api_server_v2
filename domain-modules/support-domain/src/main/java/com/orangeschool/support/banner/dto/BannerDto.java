package com.orangeschool.support.banner.dto;

import jakarta.persistence.Column;

import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.support.banner.entity.Banner;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BannerDto extends CommonDto {

    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String link;

    public static BannerDto create(Banner banner) {

        BannerDto bannerDto = BannerDto.builder()
                .originFileName(banner.getOriginFileName())
                .serverFileName(banner.getServerFileName())
                .fileUrl(banner.getFileUrl())
                .link(banner.getLink())
                .build();

        bannerDto.setCreatedAt(banner.getCreatedAt());
        bannerDto.setUpdatedAt(banner.getUpdatedAt());
        bannerDto.setId(banner.getId());

        return bannerDto;
    }
}
