package com.orangeschool.orangeschoolapiserver.domain.banner.repository;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.banner.dto.BannerV2Dto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BannerV2RepositoryCustom {

    Page<BannerV2Dto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
    List<BannerV2Dto> searchToUser(Long locationCode);
}
