package com.orangeschool.orangeschoolapiserver.domain.manager.dto;

import com.orangeschool.orangeschoolapiserver.common.enums.ManagerAuthority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateManagerDto {
    @Schema(description = "이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;
    @Schema(description = "이메일", example = "admin@naver.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email
    private String email;
    @Schema(description = "비밀번호 변경 여부", example = "false")
    private Boolean passwordChangeFlag = false;
    @Schema(description = "비밀번호(변경시에만)", example = "1q2w3e4r!@#")
    private String password;
    @Schema(description = "관리자 유형", example = "ROOT")
    private ManagerAuthority managerAuthority = ManagerAuthority.ROOT;
    @Schema(description = "접근 메뉴", example = "[]")
    private String accessMenu = "[]";
}
