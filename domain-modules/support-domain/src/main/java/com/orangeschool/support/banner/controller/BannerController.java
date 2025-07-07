package com.orangeschool.support.banner;

import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.support.banner.dto.BannerDto;
import com.orangeschool.support.banner.dto.UpdateBannerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "배너", description = "banner")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class BannerController {

    private final BannerService bannerService;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/admin/banner/{locationId}")
    public ResponseEntity<ResponseDto> create(@PathVariable(required = true) Long locationId) throws Exception {

        BannerDto bannerDto = bannerService.create(locationId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data(bannerDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/banners/{locationId}")
    public ResponseEntity<ResponseDto> get(
            @PathVariable(required = true) Long locationId)
            throws Exception {

        List<BannerDto> bannerDtoList = bannerService.get(locationId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(bannerDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/banners/{locationCode}")
    public ResponseEntity<ResponseDto> getToUser(
            @PathVariable(required = true) Long locationCode)
            throws Exception {

        List<BannerDto> bannerDtoList = bannerService.getToUser(locationCode);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(bannerDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update
    @Operation(summary = "수정")
    @PutMapping(value = "/admin/banner/{bannerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long bannerId,
                                           @ParameterObject UpdateBannerDto updateBannerDto,
                                           @RequestPart(required = false) MultipartFile file) throws Exception {

        bannerService.put(bannerId, updateBannerDto, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/banner/{bannerId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long bannerId) throws Exception {

        bannerService.delete(bannerId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
