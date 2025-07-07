package com.orangeschool.community.pick.reply.entity;

import com.orangeschool.common.entity.CommonEntity;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.pick.comment.entity.PickComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PickReply extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    private CommonMember sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId")
    private CommonMember receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickCommentId")
    private PickComment pickComment;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    public void update(String content) {
        this.content = content;
    }
}
