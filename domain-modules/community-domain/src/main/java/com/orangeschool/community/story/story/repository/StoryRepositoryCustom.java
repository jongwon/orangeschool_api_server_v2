package com.orangeschool.community.story.story.repository;


import com.orangeschool.community.story.story.dto.StoryDto;
import com.orangeschool.community.story.story.dto.StorySearchDto;
import com.orangeschool.community.story.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface StoryRepositoryCustom {
    Page<StoryDto> searchToUser(Pageable pageable, StorySearchDto storySearchDto, Long commonMemberId);
    Page<StoryDto> search(Pageable pageable, StorySearchDto storySearchDto);

    Story findFirstByOrderByTodayViewCountDesc(StorySearchDto storySearchDto, Long commonMemberId);
}
