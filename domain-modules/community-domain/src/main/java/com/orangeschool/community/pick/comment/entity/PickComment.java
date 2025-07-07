package com.orangeschool.community.pick.comment.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.pick.pick.entity.Pick;
import com.orangeschool.community.pick.reply.entity.PickReply;
import lombok.*;
import lombok.Builder;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PickComment extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickId")
    private Pick pick;

    private String content;

    @OneToMany(mappedBy = "pickComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PickReply> pickReplies = new HashSet<>();
}
