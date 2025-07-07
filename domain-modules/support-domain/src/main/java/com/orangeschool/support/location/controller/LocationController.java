package com.orangeschool.support.location;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.support.location.dto.LocationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "지역", description = "location")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LocationController {

    private final LocationService locationService;

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/locations")
    public ResponseEntity<ResponseDto> get(
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute KeywordSearchDto keywordSearchDto)
            throws Exception {

        Page<LocationDto> locationDtoPage = locationService.get(pageable, keywordSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(locationDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
