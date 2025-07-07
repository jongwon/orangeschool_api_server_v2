package com.orangeschool.support.banner.repository;

import com.orangeschool.support.banner.entity.BannerV2;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BannerV2Repository extends PagingAndSortingRepository<BannerV2, Long>, BannerV2RepositoryCustom {

}
