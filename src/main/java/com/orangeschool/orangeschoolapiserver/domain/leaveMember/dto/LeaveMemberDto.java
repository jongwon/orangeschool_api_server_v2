package com.orangeschool.orangeschoolapiserver.domain.leaveMember.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.common.enums.LeaveType;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.entity.LeaveMember;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaveMemberDto extends CommonDto {

    private MemberType memberType;
    private String memberTypeTitle;
    private String email;
    private String name;
    private LeaveType leaveType;
    private String leaveTypeTitle;
    private String reasonDetail;

    public static LeaveMemberDto create(LeaveMember leaveMember) {

        LeaveMemberDto leaveMemberDto = LeaveMemberDto.builder()
                .memberType(leaveMember.getMemberType())
                .memberTypeTitle(leaveMember.getMemberType().getTitle())
                .email(leaveMember.getEmail())
                .name(leaveMember.getName())
                .leaveType(leaveMember.getLeaveType())
                .leaveTypeTitle(leaveMember.getLeaveType().getTitle())
                .reasonDetail(leaveMember.getReasonDetail())
                .build();

        leaveMemberDto.setCreatedAt(leaveMember.getCreatedAt());
        leaveMemberDto.setUpdatedAt(leaveMember.getUpdatedAt());
        leaveMemberDto.setId(leaveMember.getId());

        return leaveMemberDto;
    }
}
