package com.orangeschool.community.pick.comment.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.util.Functions;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.pick.comment.entity.PickComment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PickCommentDto extends CommonDto {

    private Long memberId;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String parentNickName;
    private String content;
    private String dateTitle;
    private int replyCount;

    public static PickCommentDto create(PickComment pickComment) {

        CommonMember commonMember = pickComment.getCommonMember();

        PickCommentDto pickCommentDto = PickCommentDto.builder()
                .memberId(commonMember.getId())
                .originFileName(commonMember.getOriginFileName())
                .serverFileName(commonMember.getServerFileName())
                .fileUrl(commonMember.getFileUrl())
                .parentNickName(commonMember.getParentNickName())
                .content(pickComment.getContent())
                .dateTitle(Functions.getInstance().getDateTitle(pickComment.getCreatedAt()))
                .replyCount(pickComment.getPickReplies().size())
                .build();

        pickCommentDto.setCreatedAt(pickComment.getCreatedAt());
        pickCommentDto.setUpdatedAt(pickComment.getUpdatedAt());
        pickCommentDto.setId(pickComment.getId());

        return pickCommentDto;
    }


}
