package com.orangeschool.orangeschoolapiserver.domain.pick.pick.repository;

import com.orangeschool.orangeschoolapiserver.domain.pick.pick.entity.PickImage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PickImageRepository extends PagingAndSortingRepository<PickImage, Long>{
    @Transactional
    @Modifying
    @Query("delete from PickImage a where a.id in :idList")
    void deleteByIdInQuery(@Param("idList") List<Long> idList);
}
