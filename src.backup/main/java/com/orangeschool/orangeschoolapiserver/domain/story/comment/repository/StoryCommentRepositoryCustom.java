package com.orangeschool.orangeschoolapiserver.domain.story.comment.repository;


import com.orangeschool.orangeschoolapiserver.domain.story.comment.dto.StoryCommentDto;
import com.orangeschool.orangeschoolapiserver.domain.story.comment.dto.StoryCommentSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryCommentRepositoryCustom {
    Page<StoryCommentDto> search(Pageable pageable, Long storyId, StoryCommentSearchDto storyCommentSearchDto);
}
