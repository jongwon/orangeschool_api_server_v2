package com.orangeschool.community.story.like.repository;

import com.orangeschool.community.story.like.entity.StoryLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryLikeRepository extends JpaRepository<StoryLike, Long>, StoryLikeRepositoryCustom {
    Optional<StoryLike> findByCommonMemberIdAndStoryId(Long commonMemberId, Long storyId);
}
