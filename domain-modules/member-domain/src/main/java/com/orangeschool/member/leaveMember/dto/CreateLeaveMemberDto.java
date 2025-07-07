package com.orangeschool.member.leaveMember.dto;

import com.orangeschool.common.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLeaveMemberDto {

    @Schema(description = "탈퇴사유", example = "ETC", required = true)
    private LeaveType leaveType;

    @Schema(description = "탈퇴사유 입력", example = "탈퇴할래요")
    private String reasonDetail;
}
