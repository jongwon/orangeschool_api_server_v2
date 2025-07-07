package com.orangeschool.orangeschoolapiserver.domain.story.comment.repository;

import com.orangeschool.orangeschoolapiserver.domain.story.comment.entity.StoryComment;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StoryCommentRepository extends PagingAndSortingRepository<StoryComment, Long>, StoryCommentRepositoryCustom {
}
