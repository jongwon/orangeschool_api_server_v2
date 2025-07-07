package com.orangeschool.orangeschoolapiserver.domain.leaveMember.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.JoinType;
import com.orangeschool.orangeschoolapiserver.common.enums.LeaveType;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LeaveMember extends CommonEntity {

    private MemberType memberType;
    private String email;
    private String name;
    private LeaveType leaveType;
    private String reasonDetail;
}
