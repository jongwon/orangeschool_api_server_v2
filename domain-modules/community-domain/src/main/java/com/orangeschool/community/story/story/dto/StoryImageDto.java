package com.orangeschool.community.story.story.dto;

import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.community.story.story.entity.StoryImage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
//상품이미지
public class StoryImageDto extends CommonDto {

    private String serverFileName;
    private String originFileName;
    private String imageUrl;

    public static StoryImageDto create(StoryImage storyImage){
        StoryImageDto storyImageDto = StoryImageDto.builder()
                .serverFileName(storyImage.getServerFileName())
                .originFileName(storyImage.getOriginFileName())
                .imageUrl(storyImage.getImageUrl())
                .build();

        storyImageDto.setCreatedAt(storyImage.getCreatedAt());
        storyImageDto.setUpdatedAt(storyImage.getUpdatedAt());
        storyImageDto.setId(storyImage.getId());

        return storyImageDto;
    }
}
