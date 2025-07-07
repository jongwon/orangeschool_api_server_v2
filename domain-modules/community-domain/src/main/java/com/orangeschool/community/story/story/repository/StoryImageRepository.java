package com.orangeschool.community.story.story.repository;

import com.orangeschool.community.story.story.entity.StoryImage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface StoryImageRepository extends JpaRepository<StoryImage, Long>{
    @Transactional
    @Modifying
    @Query("delete from StoryImage a where a.id in :idList")
    void deleteByIdInQuery(@Param("idList") List<Long> idList);
}
