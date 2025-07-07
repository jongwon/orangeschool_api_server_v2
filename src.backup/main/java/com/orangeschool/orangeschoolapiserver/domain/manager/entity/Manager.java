package com.orangeschool.orangeschoolapiserver.domain.manager.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.ManagerAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Manager extends CommonEntity {

    private String password;
    private String name;
    private String email;
    private Boolean isActive;
    private Boolean isApproved;
    private ManagerAuthority managerAuthority;
    @Column(columnDefinition = "TEXT")
    private String accessMenu;

    public void update(String name, String email, String accessMenu, ManagerAuthority managerAuthority) {
        this.name = name;
        this.email = email;
        this.accessMenu = accessMenu;
        this.managerAuthority = managerAuthority;
    }

    public void updateActivation(Boolean activation) {
        this.isActive = activation;
    }

    public void updateApprove(Boolean approve) {
        this.isApproved = approve;
    }

    public void updateManagerAuthority(ManagerAuthority managerAuthority) {
        this.managerAuthority = managerAuthority;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
