package com.orangeschool.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    //200 OK 요청 성공
    CREATE(200, "등록되었습니다."),
    READ(200, "조회되었습니다."),
    UPDATE(200, "수정되었습니다."),
    DELETE(200, "삭제되었습니다."),
    SUCCESS(200, "정상처리되었습니다."),

    //400 BAD_REQUEST 잘못된 요청
    BAD_REQUEST(400, "잘못된 요청입니다."),
    BAD_REQUEST_EXIST_NUMBER(400, "이미 등록된 번호가 있습니다."),
    BAD_REQUEST_CHALLENGE_END(400, "완료된 챌린지입니다."),
    CLASS_EMPTY(400, "반을 입력해주세요."),
    NOTFOUND_REFERRAL_CODE(400, "초대 코드가 올바르지 않습니다."),
    EXPIRED_REFERRAL_CODE(400, "초대할 수 있는 모든 구성원이 초대되어 해당 초대 코드로 가입할 수 없습니다."),
    BAD_REQUEST_AUTH_NUMBER(400, "인증번호가 올바르지 않습니다."),

    //401 인증되지 않음
    UNAUTHORIZED_ACCOUNT(401, "계정 정보가 일치하지 않습니다."),
    UNAUTHORIZED_DORMANCY(401, "계정이 비활성화 상태입니다."),
    UNAUTHORIZED_APPROVE(401, "계정이 미 승인 상태입니다."),

    // 403 권한없음
    FORBIDDEN_INVALID_TOKEN(403, "유효하지 않은 토큰입니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    NOT_FOUND(404, "정보를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(404, "회원 정보를 찾을 수 없습니다."),
    NOT_FOUND_ACADEMY(404, "학원 정보를 찾을 수 없습니다."),
    NOT_FOUND_USER(404, "사용자 정보를 찾을 수 없습니다."),
    NOT_FOUND_FOLLOW(404, "팔로우 정보를 찾을 수 없습니다."),

    //409 CONFLICT 중복된 리소스
    CONFLICT_EMAIL(409, "이미 등록된 이메일입니다."),
    CONFLICT_ACCOUNT(409, "이미 등록된 아이디입니다."),
    CONFLICT_NICKNAME(409, "이미 등록된 닉네임입니다."),
    CONFLICT_PHONE_NUMBER(409, "이미 등록된 휴대폰 번호입니다."),
    ALREADY_FOLLOWING(409, "이미 팔로우 중입니다."),
    ALREADY_CHEERED_TODAY(409, "오늘 이미 응원했습니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버에 문제가 있습니다. 잠시후 다시 시도해주세요."),
    INTERNAL_SERVER_ERROR_SMS_CASH(500, "문자 전송 포인트가 부족합니다. 관리자에게 문의하세요.");

    private final int status;
    private final String message;
}
