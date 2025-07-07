package com.orangeschool.support.banner.repository;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.banner.dto.BannerV2Dto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BannerV2RepositoryCustom {

    Page<BannerV2Dto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
    List<BannerV2Dto> searchToUser(Long locationCode);
}
