package com.orangeschool.orangeschoolapiserver.domain.baseInfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateBaseInfoDto {
    @Schema(description = "상호명", example = "상호명", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String businessName;
    @Schema(description = "대표자", example = "대표자명", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String representative;
    @Schema(description = "사업자 등록번호", example = "000-00-00000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String businessNumber;
    @Schema(description = "주소", example = "서울시 강남구 강남로", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String address;
    @Schema(description = "전화번호", example = "00-0000-0000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String phoneNumber;
    @Schema(description = "이메일", example = "이메일", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String email;
}
