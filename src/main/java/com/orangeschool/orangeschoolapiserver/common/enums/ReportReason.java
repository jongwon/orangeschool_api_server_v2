package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ReportReason {
    LIE("거짓, 허위사실 유포"),
    THREAT("혐오 발언 및 위협"),
    LEWD("음란물 및 부적절한 댓글"),
    INJUSTICE("불법 촬영물 등 유통 신고 및 삭제 요청"),
    REPEAT("게시글/댓글 도배"),
    AD("홍보성 콘텐츠"),
    NICKNAME("닉네임/프로필 신고"),
    ETC("기타");

    private String title;

    public String getTitle() {
        return title;
    }

}
