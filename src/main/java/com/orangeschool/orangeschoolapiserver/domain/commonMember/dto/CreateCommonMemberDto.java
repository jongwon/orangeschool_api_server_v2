package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import com.orangeschool.orangeschoolapiserver.common.enums.JoinType;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

//학생유저 회원가입
@Data
public class CreateCommonMemberDto {

    @Schema(description = "회원가입유형", example = "NORMAL", required = true)
    private JoinType joinType = JoinType.NORMAL;
    @Schema(description = "소셜로그인 토큰", example = "12345")
    private String socialToken = "";
    @Schema(description = "유저 타입", example = "PARENT", required = true)
    private MemberType memberType;
    @Schema(description = "이메일", example = "user@naver.com", required = true)
    @NotBlank
    private String email;
    @Schema(description = "비밀번호", example = "1q2w3e4r!@#", required = true)
    @NotBlank
    private String password;
    @Schema(description = "성별", example = "1 or 2", required = false)
    private int gender = 1;
    @Schema(description = "이름", example = "홍길동", required = true)
    @NotBlank
    private String name;
    @Schema(description = "휴대폰 번호", example = "01012345678")
    private String phoneNumber = "";
    @Schema(description = "주소", example = "서울특별시 ~~", required = true)
    @NotBlank
    private String address;
    @Schema(description = "상세주소", example = "상세주소 1", required = true)
    @NotBlank
    private String addressDetail = "";
    @Schema(description = "생년월일", example = "97/09/09", required = true)
    private String birth = "";
    @Schema(description = "이메일 및 SMS 마케팅정보수신 동의 (선택)", example = "true")
    private Boolean agreeToSms = false;
    @Schema(description = "서비스 알림 수신 동의 (선택)", example = "true")
    private Boolean agreeToService= false;
    @Schema(description = "광고성 알림 수신 동의 (선택)", example = "true")
    private Boolean agreeToAd= false;
    @Schema(description = "push 토큰", example = "123456789")
    private String pushToken = "";
    @Schema(description = "추천인 코드", example = "A1B2C3D4")
    private String referralCode = "";
    @Schema(description = "부모 닉네임", example = "")
    private String parentNickName = "";
    @Schema(description = "닉네임", example = "")
    private String nickName = "";
    @Schema(description = "소개", example = "")
    private String intro = "";
    @Schema(description = "학교 코드", example = "")
    private String schoolCode = "";
    @Schema(description = "학교 명", example = "")
    private String schoolName = "";
    @Schema(description = "학년", example = "")
    private String grade = "";
    @Schema(description = "반", example = "")
    private String schoolClass = "";
    @Schema(description = "번호", example = "")
    private String classNumber = "";
    @Schema(description = "부모 고유 아이디", example = "")
    private Long parentId = 0L;
}