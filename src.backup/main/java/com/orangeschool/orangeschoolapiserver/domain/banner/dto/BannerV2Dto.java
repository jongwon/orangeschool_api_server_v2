package com.orangeschool.orangeschoolapiserver.domain.banner.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.banner.entity.BannerV2;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BannerV2Dto extends CommonDto {

    private String title;
    private String regionCodeTag;
    private String regionNameTag;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String link;
    private Boolean isActive;
    private Long viewCount;

    public static BannerV2Dto create(BannerV2 bannerV2) {

        BannerV2Dto bannerV2Dto = BannerV2Dto.builder()
                .title(bannerV2.getTitle())
                .regionCodeTag(bannerV2.getRegionCodeTag())
                .regionNameTag(bannerV2.getRegionNameTag())
                .originFileName(bannerV2.getOriginFileName())
                .serverFileName(bannerV2.getServerFileName())
                .fileUrl(bannerV2.getFileUrl())
                .link(bannerV2.getLink())
                .isActive(bannerV2.getIsActive())
                .viewCount(bannerV2.getViewCount())
                .build();

        bannerV2Dto.setCreatedAt(bannerV2.getCreatedAt());
        bannerV2Dto.setUpdatedAt(bannerV2.getUpdatedAt());
        bannerV2Dto.setId(bannerV2.getId());

        return bannerV2Dto;
    }
}
