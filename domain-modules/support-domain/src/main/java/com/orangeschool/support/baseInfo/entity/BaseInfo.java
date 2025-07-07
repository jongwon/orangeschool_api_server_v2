package com.orangeschool.support.baseInfo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;

import com.orangeschool.common.entity.BaseEntity;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BaseInfo extends BaseEntity {

    private String businessName;
    private String representative;
    private String businessNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private String iosVersion;
    private String androidVersion;

    public void update(String businessName, String representative, String businessNumber, String address,
            String phoneNumber, String email) {
        this.businessName = businessName;
        this.representative = representative;
        this.businessNumber = businessNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
