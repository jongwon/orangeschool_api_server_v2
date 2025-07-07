package com.orangeschool.support.notice.repository;


import com.orangeschool.support.notice.entity.Notice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoticeRepository extends PagingAndSortingRepository<Notice, Long>, NoticeRepositoryCustom {

}
