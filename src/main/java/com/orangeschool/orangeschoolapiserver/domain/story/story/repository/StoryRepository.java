package com.orangeschool.orangeschoolapiserver.domain.story.story.repository;

import com.orangeschool.orangeschoolapiserver.domain.story.story.entity.Story;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface StoryRepository extends PagingAndSortingRepository<Story, Long>, StoryRepositoryCustom {

    @Transactional
    @Modifying
    @Query("UPDATE Story s SET s.todayViewCount = 0")
    void resetTodayViewCount();
}
