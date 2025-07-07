package com.orangeschool.support.banner;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.support.banner.dto.BannerV2Dto;
import com.orangeschool.support.banner.dto.CreateBannerV2Dto;
import com.orangeschool.support.banner.dto.UpdateBannerV2Dto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "배너V2", description = "bannerV2")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class BannerV2Controller {

    private final BannerV2Service bannerV2Service;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/admin/bannerV2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> create(@ParameterObject CreateBannerV2Dto createBannerV2Dto,
                                              @RequestPart(required = true) MultipartFile file) throws Exception {

        bannerV2Service.create(createBannerV2Dto, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/bannerV2s")
    public ResponseEntity<ResponseDto> get(
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute KeywordSearchDto keywordSearchDto) throws Exception {

        Page<BannerV2Dto> bannerV2DtoPage = bannerV2Service.get(pageable, keywordSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(bannerV2DtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/bannerV2s")
    public ResponseEntity<ResponseDto> getToUser(@RequestHeader(name = "Authorization") String token) throws Exception {

        List<BannerV2Dto> bannerV2DtoList = bannerV2Service.getToUser(jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(bannerV2DtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/bannerV2/{bannerV2Id}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long bannerV2Id) throws Exception {

        BannerV2Dto bannerV2Dto = bannerV2Service.getById(bannerV2Id, false);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(bannerV2Dto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "배너 클릭 시")
    @GetMapping(value = "/user/bannerV2/click/{bannerV2Id}")
    public ResponseEntity<ResponseDto> getByIdToUser(@PathVariable(required = true) Long bannerV2Id) throws Exception {

        BannerV2Dto bannerV2Dto = bannerV2Service.getById(bannerV2Id, true);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(bannerV2Dto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // put
    @Operation(summary = "수정")
    @PutMapping(value = "/admin/bannerV2/{bannerV2Id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long bannerV2Id,
                                           @RequestPart(required = false) MultipartFile file,
                                           @ParameterObject UpdateBannerV2Dto updateBannerV2Dto) throws Exception {

        bannerV2Service.put(bannerV2Id, updateBannerV2Dto, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "활성상태 수정")
    @PutMapping(value = "/admin/bannerV2/activation/{bannerV2Id}")
    public ResponseEntity<ResponseDto> putActivation(@PathVariable(required = true) Long bannerV2Id,
                                                     @RequestBody UpdateActivationDto updateActivationDto) throws Exception {

        bannerV2Service.putActivation(bannerV2Id, updateActivationDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/bannerV2/{bannerV2Id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long bannerV2Id) throws Exception {

        bannerV2Service.delete(bannerV2Id);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/bannerV2s", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> deleteAll(@ModelAttribute IdListDto idListDto) throws Exception {

        bannerV2Service.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}