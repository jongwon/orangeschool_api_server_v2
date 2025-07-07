package com.orangeschool.orangeschoolapiserver.domain.baseInfo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.domain.baseInfo.dto.BaseInfoDto;
import com.orangeschool.orangeschoolapiserver.domain.baseInfo.dto.UpdateBaseInfoDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "기본정보", description = "baseInfo")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class BaseInfoController {

    private final BaseInfoService baseInfoService;

    // get
    @Operation(summary = "기본정보 조회")
    @GetMapping(value = "/common/baseInfo")
    public ResponseEntity<ResponseDto> get() throws Exception {

        BaseInfoDto baseInfoDto = baseInfoService.get();

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(baseInfoDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // put
    @Operation(summary = "기본정보 수정")
    @PutMapping(value = "/admin/baseInfo")
    public ResponseEntity<ResponseDto> put(@RequestBody @Validated UpdateBaseInfoDto updateBaseInfoDto)
            throws Exception {

        baseInfoService.put(updateBaseInfoDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
