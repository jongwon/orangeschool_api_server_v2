package com.orangeschool.orangeschoolapiserver.domain.location.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.domain.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Location extends CommonEntity {

    private long code;
    private String title;
    
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<Banner> banners = new HashSet<>();
}
