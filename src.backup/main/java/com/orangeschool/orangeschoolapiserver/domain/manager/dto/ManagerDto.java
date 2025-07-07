package com.orangeschool.orangeschoolapiserver.domain.manager.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.common.enums.ManagerAuthority;
import com.orangeschool.orangeschoolapiserver.domain.manager.entity.Manager;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagerDto extends CommonDto {

    private String name;
    private String email;
    private Boolean isActive;
    private Boolean isApproved;
    private ManagerAuthority managerAuthority;
    private String managerAuthorityTitle;
    private String accessMenu;

    public static ManagerDto create(Manager manager) {

        ManagerDto managerDto = ManagerDto.builder()
                .name(manager.getName())
                .email(manager.getEmail())
                .managerAuthority(manager.getManagerAuthority())
                .managerAuthorityTitle(manager.getManagerAuthority().getTitle())
                .isActive(manager.getIsActive())
                .isApproved(manager.getIsApproved())
                .accessMenu(manager.getAccessMenu())
                .build();

        managerDto.setCreatedAt(manager.getCreatedAt());
        managerDto.setUpdatedAt(manager.getUpdatedAt());
        managerDto.setId(manager.getId());

        return managerDto;
    }
}
