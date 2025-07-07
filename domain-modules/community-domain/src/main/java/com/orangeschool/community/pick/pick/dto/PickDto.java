package com.orangeschool.community.pick.pick.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.enums.PickType;
import com.orangeschool.community.pick.pick.entity.Pick;
import lombok.Builder;
import lombok.EqualsAndHashCode;import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)@Data
@Builder
public class PickDto extends CommonDto {

    private Long number;
    private PickType pickType;
    private String pickTypeTitle;
    private String title;
    private String content;
    private String link;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String writerEmail;

    private String previewContent;
    private String linkBtnName;
    private List<PickImageDto> images;
    private int commentCount;
    private int likeCount;
    private boolean isLike; // 좋아요
    private boolean isActive;
    private Long viewCount; // 조회수

    public static PickDto create(Pick pick) {

        PickDto pickDto = PickDto.builder()
                .number(pick.getNumber())
                .pickType(pick.getPickType())
                .pickTypeTitle(pick.getPickType().getTitle())
                .title(pick.getTitle())
                .content(pick.getContent())
                .link(pick.getLink())
                .originFileName(pick.getOriginFileName())
                .serverFileName(pick.getServerFileName())
                .fileUrl(pick.getFileUrl())
                .writerEmail(pick.getWriterEmail())

                .previewContent(pick.getPreviewContent())
                .linkBtnName(pick.getLinkBtnName())
                .images(pick.getImages().stream().map(PickImageDto::create).collect(Collectors.toList()))
                .commentCount(pick.getTotalCommentCount())
                .likeCount(pick.getPickLikes().size())
                .isActive(pick.getIsActive())
                .viewCount(pick.getViewCount())
                .build();

        pickDto.setCreatedAt(pick.getCreatedAt());
        pickDto.setUpdatedAt(pick.getUpdatedAt());
        pickDto.setId(pick.getId());

        return pickDto;
    }

    public static PickDto create(Pick pick, Long commonMemberId) {
        PickDto pickDto = PickDto.builder()
                .number(pick.getNumber())
                .pickType(pick.getPickType())
                .pickTypeTitle(pick.getPickType().getTitle())
                .title(pick.getTitle())
                .content(pick.getContent())
                .link(pick.getLink())
                .originFileName(pick.getOriginFileName())
                .serverFileName(pick.getServerFileName())
                .fileUrl(pick.getFileUrl())
                .writerEmail(pick.getWriterEmail())

                .previewContent(pick.getPreviewContent())
                .linkBtnName(pick.getLinkBtnName())
                .images(pick.getImages().stream().map(PickImageDto::create).collect(Collectors.toList()))
                .isLike(pick.getPickLikes().stream().anyMatch(pickLike -> pickLike.getCommonMember().getId().equals(commonMemberId)))
                .commentCount(pick.getTotalCommentCount())
                .likeCount(pick.getPickLikes().size())
                .isActive(pick.getIsActive())
                .viewCount(pick.getViewCount())
                .build();

        pickDto.setCreatedAt(pick.getCreatedAt());
        pickDto.setUpdatedAt(pick.getUpdatedAt());
        pickDto.setId(pick.getId());

        return pickDto;
    }
}
