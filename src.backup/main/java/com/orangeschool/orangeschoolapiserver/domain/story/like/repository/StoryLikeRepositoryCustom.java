package com.orangeschool.orangeschoolapiserver.domain.story.like.repository;


import com.orangeschool.orangeschoolapiserver.domain.story.like.dto.StoryLikeDto;
import com.orangeschool.orangeschoolapiserver.domain.story.like.dto.StoryLikeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryLikeRepositoryCustom {
    Page<StoryLikeDto> search(Pageable pageable,Long storyId, StoryLikeSearchDto storyCommentSearchDto);
}
