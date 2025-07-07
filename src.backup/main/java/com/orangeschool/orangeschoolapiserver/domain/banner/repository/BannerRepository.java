package com.orangeschool.orangeschoolapiserver.domain.banner.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.orangeschool.orangeschoolapiserver.domain.banner.entity.Banner;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends PagingAndSortingRepository<Banner, Long>, BannerRepositoryCustom {
    List<Banner> findByLocationId(Long locationId);
}
