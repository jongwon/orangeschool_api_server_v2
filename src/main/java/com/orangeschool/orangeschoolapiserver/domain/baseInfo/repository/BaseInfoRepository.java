package com.orangeschool.orangeschoolapiserver.domain.baseInfo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.orangeschool.orangeschoolapiserver.domain.baseInfo.entity.BaseInfo;

import java.util.Optional;

public interface BaseInfoRepository extends PagingAndSortingRepository<BaseInfo, Long> {
}
