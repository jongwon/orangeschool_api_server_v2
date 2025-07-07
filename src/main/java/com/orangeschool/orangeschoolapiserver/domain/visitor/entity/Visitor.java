package com.orangeschool.orangeschoolapiserver.domain.visitor.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Visitor extends CommonEntity {

    private LocalDate today;
    private Integer count;
    private MemberType memberType;

    public void updateVisitors() {
        this.count += 1;
    }
}
