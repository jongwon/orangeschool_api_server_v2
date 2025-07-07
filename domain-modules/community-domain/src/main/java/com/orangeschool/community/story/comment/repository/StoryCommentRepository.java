package com.orangeschool.community.story.comment.repository;

import com.orangeschool.community.story.comment.entity.StoryComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryCommentRepository extends JpaRepository<StoryComment, Long>, StoryCommentRepositoryCustom {
}
