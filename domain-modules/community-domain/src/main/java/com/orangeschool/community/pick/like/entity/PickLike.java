package com.orangeschool.community.pick.like.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.pick.pick.entity.Pick;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PickLike extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickId")
    private Pick pick;
}
