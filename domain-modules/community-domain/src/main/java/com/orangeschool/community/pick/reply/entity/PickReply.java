package com.orangeschool.community.pick.reply.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.pick.comment.entity.PickComment;
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
public class PickReply extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickCommentId")
    private PickComment pickComment;

    private String content;
}
