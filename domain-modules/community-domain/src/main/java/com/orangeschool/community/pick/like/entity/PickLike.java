package com.orangeschool.community.pick.like.entity;

import com.orangeschool.common.entity.CommonEntity;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.pick.pick.entity.Pick;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PickLike extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickId")
    private Pick pick;
}
