package com.orangeschool.member.leaveMember.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.JoinType;
import com.orangeschool.common.enums.LeaveType;
import com.orangeschool.common.enums.MemberType;
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
public class LeaveMember extends BaseEntity {

    private MemberType memberType;
    private String email;
    private String name;
    private LeaveType leaveType;
    private String reasonDetail;
}
