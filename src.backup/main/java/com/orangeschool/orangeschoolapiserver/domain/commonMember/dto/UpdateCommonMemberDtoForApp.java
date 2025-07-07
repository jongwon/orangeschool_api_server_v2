package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;


@Data
public class UpdateCommonMemberDtoForApp {

    @Schema(description = "성별", example = "1 or 2", required = false)
    private int gender;
    @Schema(description = "이름", example = "홍길동", required = true)
    @NotBlank
    private String name;
    @Schema(description = "이메일 변경여부", example = "false", required = true)
    private Boolean changeEmail = false;
    @Schema(description = "이메일", example = "user@naver.com", required = true)
    @NotBlank
    private String email;
    @Schema(description = "주소 변경여부", example = "false", required = true)
    private Boolean changeAddress = false;
    @Schema(description = "주소", example = "서울특별시 ~~", required = true)
    @NotBlank
    private String address;
    @Schema(description = "상세주소", example = "상세주소 1", required = true)
    @NotBlank
    private String addressDetail;
    @Schema(description = "생년월일", example = "97/09/09", required = true)
    @NotBlank
    private String birth;
    @Schema(description = "파일 삭제 여부", example = "false")
    private Boolean deleteFileFlag = false;
    @Schema(description = "부모 닉네임", example = "")
    private String parentNickName = "";
    @Schema(description = "부모 닉네임 변경여부", example = "false", required = true)
    private Boolean changeParentNickName = false;
    @Schema(description = "닉네임", example = "아이1")
    private String nickName = "";
    @Schema(description = "소개", example = "아이입니다.")
    private String intro = "";
    @Schema(description = "비밀번호 변경여부", example = "false", required = true)
    private Boolean changePassword = false;
    @Schema(description = "비밀번호", example = "1q2w3e4r!@#", required = true)
    @NotBlank
    private String password;
    @Schema(description = "전화번호", example = "01012345678", required = false)
    private String phoneNumber;
}