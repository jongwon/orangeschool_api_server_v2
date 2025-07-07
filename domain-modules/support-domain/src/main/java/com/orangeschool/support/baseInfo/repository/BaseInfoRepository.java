package com.orangeschool.support.baseInfo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.orangeschool.support.baseInfo.entity.BaseInfo;

import java.util.Optional;

public interface BaseInfoRepository extends PagingAndSortingRepository<BaseInfo, Long> {
}
