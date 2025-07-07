package com.orangeschool.community.story.story.repository;

import com.orangeschool.community.story.story.entity.Story;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {

    @Transactional
    @Modifying
    @Query("UPDATE Story s SET s.todayViewCount = 0")
    void resetTodayViewCount();
}
