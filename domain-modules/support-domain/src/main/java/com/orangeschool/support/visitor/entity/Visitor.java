package com.orangeschool.support.visitor.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Visitor extends BaseEntity {

    private LocalDate today;
    private Integer count;
    private MemberType memberType;

    public void updateVisitors() {
        this.count += 1;
    }
}
