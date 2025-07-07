package com.orangeschool.community.story.like.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.community.story.like.entity.StoryLike;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoryLikeDto extends CommonDto {

    private String parentNickName;
    private String fileUrl;

    public static StoryLikeDto create(StoryLike storyComment) {

        StoryLikeDto storyCommentDto = StoryLikeDto.builder()
                .parentNickName(storyComment.getCommonMember().getParentNickName())
                .fileUrl(storyComment.getCommonMember().getFileUrl())
                .build();

        storyCommentDto.setCreatedAt(storyComment.getCreatedAt());
        storyCommentDto.setUpdatedAt(storyComment.getUpdatedAt());
        storyCommentDto.setId(storyComment.getId());

        return storyCommentDto;
    }


}
