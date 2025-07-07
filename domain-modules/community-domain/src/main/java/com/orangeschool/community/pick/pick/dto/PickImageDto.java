package com.orangeschool.community.pick.pick.dto;

import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.community.pick.pick.entity.PickImage;
import lombok.Builder;
import lombok.EqualsAndHashCode;import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper = false)@Data
@Builder
//상품이미지
public class PickImageDto extends CommonDto {

    private String serverFileName;
    private String originFileName;
    private String imageUrl;

    public static PickImageDto create(PickImage pickImage){
        PickImageDto pickImageDto = PickImageDto.builder()
                .serverFileName(pickImage.getServerFileName())
                .originFileName(pickImage.getOriginFileName())
                .imageUrl(pickImage.getImageUrl())
                .build();

        pickImageDto.setCreatedAt(pickImage.getCreatedAt());
        pickImageDto.setUpdatedAt(pickImage.getUpdatedAt());
        pickImageDto.setId(pickImage.getId());

        return pickImageDto;
    }
}
