package com.orangeschool.support.banner.repository;

import com.orangeschool.support.banner.entity.Banner;

import java.util.List;

public interface BannerRepositoryCustom {

    List<Banner> searchByLocationCode(Long locationCode);
}
