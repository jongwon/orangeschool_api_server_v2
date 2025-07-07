package com.orangeschool.community.pick.comment.entity;

import com.orangeschool.common.entity.CommonEntity;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.pick.pick.entity.Pick;
import com.orangeschool.community.pick.reply.entity.PickReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PickComment extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickId")
    private Pick pick;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @OneToMany(mappedBy = "pickComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<PickReply> pickReplies = new HashSet<>();

    public void update(String content) {
        this.content = content;
    }
}
