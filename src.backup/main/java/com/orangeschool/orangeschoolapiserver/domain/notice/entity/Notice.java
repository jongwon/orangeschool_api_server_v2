package com.orangeschool.orangeschoolapiserver.domain.notice.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.domain.memberNotice.entity.MemberNotice;
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
public class Notice extends CommonEntity {

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String writerEmail;

}
