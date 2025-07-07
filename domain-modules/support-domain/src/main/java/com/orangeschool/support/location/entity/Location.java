package com.orangeschool.support.location.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.banner.entity.Banner;
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
public class Location extends BaseEntity {

    private long code;
    private String title;
    
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    @Builder.Default
    private Set<Banner> banners = new HashSet<>();

}
