package com.orangeschool.orangeschoolapiserver.domain.commonMember.controller;

import com.orangeschool.orangeschoolapiserver.common.dto.request.UpdateActivationDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.CommonMemberService;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.UpdateCommonMemberDto;
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

@Tag(name = "회원 관리 (관리자)", description = "member management for admin")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AdminMemberController {

    private final CommonMemberService commonMemberService;

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/commonMembers")
    public ResponseEntity<ResponseDto> get(
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute CommonMemberFilterDto commonMemberFilter) throws Exception {

        Page<CommonMemberDto> commonMemberDtoPage = commonMemberService.get(pageable, commonMemberFilter);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/commonMember/{commonMemberId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long commonMemberId) throws Exception {

        CommonMemberDto commonMemberDto = commonMemberService.getById(commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "자녀 목록 조회")
    @GetMapping(value = "/admin/commonMembers/{commonMemberId}")
    public ResponseEntity<ResponseDto> getChildList(@PathVariable(required = true) Long commonMemberId,
                                                    @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<CommonMemberDto> commonMemberDtoPage = commonMemberService.getChildList(commonMemberId, pageable);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "수정")
    @PutMapping(value = "/admin/commonMember/{commonMemberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long commonMemberId,
                                           @RequestPart(required = false) MultipartFile file,
                                           @ParameterObject UpdateCommonMemberDto updateCommonMemberDto) throws Exception {

        commonMemberService.put(commonMemberId, updateCommonMemberDto, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "활성상태 수정")
    @PutMapping(value = "/admin/commonMember/activation/{commonMemberId}")
    public ResponseEntity<ResponseDto> putActivation(@PathVariable(required = true) Long commonMemberId,
                                                     @RequestBody UpdateActivationDto updateActivationDto) throws Exception {

        commonMemberService.putActivation(commonMemberId, updateActivationDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/commonMember/{commonMemberId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long commonMemberId) throws Exception {

        commonMemberService.delete(commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}