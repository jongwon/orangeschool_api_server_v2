package com.orangeschool.community.story.story.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.util.Functions;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.story.story.entity.Story;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class StoryDto extends CommonDto {

    private Long memberId;
    private String email;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String parentNickName;
    private String title;
    private String content;
    private String regionNameTag;
    private String regionCodeTag;
    private List<StoryImageDto> images;
    private String dateTitle;
    private int commentCount;
    private int likeCount;
    private boolean isPopular; // 인기글
    private boolean isLike; // 좋아요
    private boolean isActive;
    private Long viewCount; // 조회수

    public static StoryDto create(Story story) {

        CommonMember commonMember = story.getCommonMember();

        StoryDto storyDto = StoryDto.builder()
                .memberId(commonMember.getId())
                .email(commonMember.getEmail())
                .originFileName(commonMember.getOriginFileName())
                .serverFileName(commonMember.getServerFileName())
                .fileUrl(commonMember.getFileUrl())
                .parentNickName(commonMember.getParentNickName())
                .title(story.getTitle())
                .content(story.getContent())
                .regionNameTag(story.getRegionNameTag())
                .regionCodeTag(story.getRegionCodeTag())
                .images(story.getImages().stream().map(StoryImageDto::create).collect(Collectors.toList()))
                .dateTitle(Functions.getInstance().getDateTitle(story.getCreatedAt()))
                .commentCount(story.getTotalCommentCount())
                .likeCount(story.getStoryLikes().size())
                .isPopular(story.isPopular())
                .isActive(story.getIsActive())
                .viewCount(story.getViewCount())
                .build();

        storyDto.setCreatedAt(story.getCreatedAt());
        storyDto.setUpdatedAt(story.getUpdatedAt());
        storyDto.setId(story.getId());

        return storyDto;
    }

    public static StoryDto create(Story story, Long commonMemberId) {

        CommonMember commonMember = story.getCommonMember();

        StoryDto storyDto = StoryDto.builder()
                .memberId(commonMember.getId())
                .originFileName(commonMember.getOriginFileName())
                .serverFileName(commonMember.getServerFileName())
                .fileUrl(commonMember.getFileUrl())
                .parentNickName(commonMember.getParentNickName())
                .title(story.getTitle())
                .content(story.getContent())
                .regionNameTag(story.getRegionNameTag())
                .regionCodeTag(story.getRegionCodeTag())
                .images(story.getImages().stream().map(StoryImageDto::create).collect(Collectors.toList()))
                .dateTitle(Functions.getInstance().getDateTitle(story.getCreatedAt()))
                .commentCount(story.getTotalCommentCount())
                .likeCount(story.getStoryLikes().size())
                .isPopular(story.isPopular())
                .isLike(story.getStoryLikes().stream().anyMatch(storyLike -> storyLike.getCommonMember().getId().equals(commonMemberId)))
                .isActive(story.getIsActive())
                .viewCount(story.getViewCount())
                .build();

        storyDto.setCreatedAt(story.getCreatedAt());
        storyDto.setUpdatedAt(story.getUpdatedAt());
        storyDto.setId(story.getId());

        return storyDto;
    }


}
