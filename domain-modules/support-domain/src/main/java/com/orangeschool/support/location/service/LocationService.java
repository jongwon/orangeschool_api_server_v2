package com.orangeschool.support.location;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.location.dto.LocationDto;
import com.orangeschool.support.location.entity.Location;
import com.orangeschool.support.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public Page<LocationDto> get(Pageable pageable, KeywordSearchDto keywordSearchDto) throws Exception {
        return locationRepository.search(pageable, keywordSearchDto);
    }

    public long getLocationCode(String address) {
        String locationKeyword;

        if(address.startsWith("충남")){
            locationKeyword = "충청남도";
        }else if(address.startsWith("경북")){
            locationKeyword = "경상북도";
        }else if(address.startsWith("충북")){
            locationKeyword = "충청북도";
        }else if(address.startsWith("전남")){
            locationKeyword = "전라남도";
        }else if(address.startsWith("전북")){
            locationKeyword = "전라북도";
        }else if(address.startsWith("경남")){
            locationKeyword = "경상남도";
        } else {
            locationKeyword = address.substring(0, 2);
        }
        List<Location> locationList = locationRepository.findAll();
        Optional<Location> locationOptional = locationList.stream().filter(item -> item.getTitle().contains(locationKeyword)).findFirst();

        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            return location.getCode();
        } else {
            return 0L;
        }
    }
}
