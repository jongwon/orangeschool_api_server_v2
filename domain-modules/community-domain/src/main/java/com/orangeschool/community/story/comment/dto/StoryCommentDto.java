package com.orangeschool.community.story.comment.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.util.Functions;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.story.comment.entity.StoryComment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoryCommentDto extends CommonDto {

    private Long memberId;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String parentNickName;
    private String content;
    private String dateTitle;
    private int replyCount;

    public static StoryCommentDto create(StoryComment storyComment) {

        CommonMember commonMember = storyComment.getCommonMember();

        StoryCommentDto storyCommentDto = StoryCommentDto.builder()
                .memberId(commonMember.getId())
                .originFileName(commonMember.getOriginFileName())
                .serverFileName(commonMember.getServerFileName())
                .fileUrl(commonMember.getFileUrl())
                .parentNickName(commonMember.getParentNickName())
                .content(storyComment.getContent())
                .dateTitle(Functions.getInstance().getDateTitle(storyComment.getCreatedAt()))
                .replyCount(storyComment.getStoryReplies().size())
                .build();

        storyCommentDto.setCreatedAt(storyComment.getCreatedAt());
        storyCommentDto.setUpdatedAt(storyComment.getUpdatedAt());
        storyCommentDto.setId(storyComment.getId());

        return storyCommentDto;
    }


}
