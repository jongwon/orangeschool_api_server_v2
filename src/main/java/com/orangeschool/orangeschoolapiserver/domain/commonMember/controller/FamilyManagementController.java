package com.orangeschool.orangeschoolapiserver.domain.commonMember.controller;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.CommonMemberServiceV2;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.UpdateReferralConfirmDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "가족/구성원 관리", description = "family management")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class FamilyManagementController {

    private final CommonMemberServiceV2 commonMemberServiceV2;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "구성원 요청 목록 조회")
    @GetMapping(value = "/user/v2/referral/request")
    public ResponseEntity<ResponseDto> getReferralRequest(@RequestHeader(name = "Authorization") String token) throws Exception {

        List<CommonMemberDto> commonMemberDtoList = commonMemberServiceV2.getReferralRequest(jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "구성원 요청 목록 조회 (관리자)")
    @GetMapping(value = "/admin/v2/commonMember/family/{commonMemberId}")
    public ResponseEntity<ResponseDto> getFamily(@PathVariable(required = true) Long commonMemberId) throws Exception {

        List<CommonMemberDto> commonMemberDtoList = commonMemberServiceV2.getReferralRequest(commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "구성원 승인or반려or삭제")
    @PostMapping(value = "/user/v2/referral/request/confirm/{commonMemberId}")
    public ResponseEntity<ResponseDto> postReferralRequestConfirm(@RequestHeader(name = "Authorization") String token,
                                                                  @PathVariable(required = true) Long commonMemberId,
                                                                  @RequestBody @Validated UpdateReferralConfirmDto updateReferralConfirmDto) throws Exception {

        commonMemberServiceV2.postReferralRequestConfirm(jwtTokenProvider.getId(token), commonMemberId, updateReferralConfirmDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}