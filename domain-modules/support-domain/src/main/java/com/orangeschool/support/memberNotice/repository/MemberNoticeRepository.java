package com.orangeschool.support.memberNotice.repository;


import com.orangeschool.support.memberNotice.entity.MemberNotice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberNoticeRepository extends PagingAndSortingRepository<MemberNotice, Long>, MemberNoticeRepositoryCustom {
    @Transactional
    @Modifying
    @Query("delete from MemberNotice a where a.noticeId in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
