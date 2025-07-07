package com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.entity.PickImage;
import lombok.Builder;
import lombok.Data;

@Data
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
