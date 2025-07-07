package com.orangeschool.community.pick.like.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.community.pick.like.entity.PickLike;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PickLikeDto extends CommonDto {

    private String parentNickName;
    private String fileUrl;

    public static PickLikeDto create(PickLike pickComment) {

        PickLikeDto pickCommentDto = PickLikeDto.builder()
                .parentNickName(pickComment.getCommonMember().getParentNickName())
                .fileUrl(pickComment.getCommonMember().getFileUrl())
                .build();

        pickCommentDto.setCreatedAt(pickComment.getCreatedAt());
        pickCommentDto.setUpdatedAt(pickComment.getUpdatedAt());
        pickCommentDto.setId(pickComment.getId());

        return pickCommentDto;
    }


}
