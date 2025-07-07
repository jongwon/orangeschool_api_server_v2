package com.orangeschool.orangeschoolapiserver.domain.banner.repository;

import com.orangeschool.orangeschoolapiserver.domain.banner.entity.BannerV2;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BannerV2Repository extends PagingAndSortingRepository<BannerV2, Long>, BannerV2RepositoryCustom {

}
