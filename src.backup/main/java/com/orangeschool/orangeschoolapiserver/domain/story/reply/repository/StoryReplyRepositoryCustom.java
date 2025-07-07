package com.orangeschool.orangeschoolapiserver.domain.story.reply.repository;


import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.StoryReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.StoryReplySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface StoryReplyRepositoryCustom {
    Page<StoryReplyDto> search(Pageable pageable,Long storyCommentId, StoryReplySearchDto storyReplySearchDto);
}
