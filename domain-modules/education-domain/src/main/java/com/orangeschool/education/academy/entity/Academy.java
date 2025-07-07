package com.orangeschool.education.academy.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.education.memberAcademy.entity.MemberAcademy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Academy extends BaseEntity {

    private String academyName;
    private String address;
    private String addressDetail;
    private Boolean isAdmin;
    private String postUserEmail;

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<MemberAcademy> memberAcademies = new HashSet<>();

    public void update(String academyName, String address, String addressDetail) {
        this.academyName = academyName;
        this.address = address;
        this.addressDetail = addressDetail;
    }
}
