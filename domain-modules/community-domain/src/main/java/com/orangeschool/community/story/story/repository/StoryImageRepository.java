package com.orangeschool.community.story.story.repository;

import com.orangeschool.community.story.story.entity.StoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface StoryImageRepository extends JpaRepository<StoryImage, Long> {
    List<StoryImage> findByStoryId(Long storyId);
    
    @Transactional
    void deleteByImageUrl(String imageUrl);
}
