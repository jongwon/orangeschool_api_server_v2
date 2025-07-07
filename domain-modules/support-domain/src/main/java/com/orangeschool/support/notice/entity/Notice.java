package com.orangeschool.support.notice.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.memberNotice.entity.MemberNotice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notice extends BaseEntity {

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String writerEmail;

}
