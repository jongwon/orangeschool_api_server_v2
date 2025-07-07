package com.orangeschool.orangeschoolapiserver.domain.academy.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.entity.Academy;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcademyDto extends CommonDto {

    private String academyName;
    private String address;
    private String addressDetail;
    private Boolean isAdmin;
    private String postUserEmail;

    public static AcademyDto create(Academy academy) {

        AcademyDto academyDto = AcademyDto.builder()
                .academyName(academy.getAcademyName())
                .address(academy.getAddress())
                .addressDetail(academy.getAddressDetail())
                .isAdmin(academy.getIsAdmin())
                .postUserEmail(academy.getPostUserEmail())
                .build();

        academyDto.setCreatedAt(academy.getCreatedAt());
        academyDto.setUpdatedAt(academy.getUpdatedAt());
        academyDto.setId(academy.getId());

        return academyDto;
    }
}
