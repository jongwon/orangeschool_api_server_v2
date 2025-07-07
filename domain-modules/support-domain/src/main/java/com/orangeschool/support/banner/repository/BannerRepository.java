package com.orangeschool.support.banner.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.orangeschool.support.banner.entity.Banner;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends PagingAndSortingRepository<Banner, Long>, BannerRepositoryCustom {
    List<Banner> findByLocationId(Long locationId);
}
