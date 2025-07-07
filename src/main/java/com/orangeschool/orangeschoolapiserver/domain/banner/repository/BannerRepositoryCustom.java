package com.orangeschool.orangeschoolapiserver.domain.banner.repository;

import com.orangeschool.orangeschoolapiserver.domain.banner.entity.Banner;

import java.util.List;

public interface BannerRepositoryCustom {

    List<Banner> searchByLocationCode(Long locationCode);
}
