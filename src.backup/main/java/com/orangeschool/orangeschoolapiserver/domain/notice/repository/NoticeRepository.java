package com.orangeschool.orangeschoolapiserver.domain.notice.repository;


import com.orangeschool.orangeschoolapiserver.domain.notice.entity.Notice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoticeRepository extends PagingAndSortingRepository<Notice, Long>, NoticeRepositoryCustom {

}
