package com.orangeschool.orangeschoolapiserver.domain.commonMember.controller;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.CommonMemberService;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.CommonMemberServiceV2;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "회원 프로필", description = "member profile")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberProfileController {

    private final CommonMemberService commonMemberService;
    private final CommonMemberServiceV2 commonMemberServiceV2;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "본인 정보 조회")
    @GetMapping(value = "/user/commonMember")
    public ResponseEntity<ResponseDto> getMyInfo(@RequestHeader(name = "Authorization") String token) throws Exception {

        CommonMemberDto commonMemberDto = commonMemberService.getMyInfo(jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "회원 수정")
    @PutMapping(value = "/user/commonMember", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> putMyInfo(@RequestHeader(name = "Authorization") String token,
                                                 @RequestPart(required = false) MultipartFile file,
                                                 @ParameterObject UpdateCommonMemberDtoForApp updateCommonMemberDtoForApp) throws Exception {

        commonMemberService.putMyInfo(jwtTokenProvider.getId(token), updateCommonMemberDtoForApp, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "회원 알림 설정")
    @PutMapping(value = "/user/commonMember/setting/alarm")
    public ResponseEntity<ResponseDto> putSettingAlarm(@RequestHeader(name = "Authorization") String token,
                                                       @RequestBody @Validated UpdateSettingAlarmDto updateSettingAlarmDto) throws Exception {

        commonMemberService.putSettingAlarm(jwtTokenProvider.getId(token), updateSettingAlarmDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "회원 우리 동네 설정")
    @PutMapping(value = "/user/v2/commonMember/setting/region")
    public ResponseEntity<ResponseDto> putSettingRegion(@RequestHeader(name = "Authorization") String token,
                                                        @RequestBody @Validated UpdateSettingRegionDto updateSettingRegionDto) throws Exception {

        commonMemberServiceV2.putSettingRegion(jwtTokenProvider.getId(token), updateSettingRegionDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "회원 닉네임 수정")
    @PutMapping(value = "/user/v2/commonMember")
    public ResponseEntity<ResponseDto> putParentNickname(@RequestHeader(name = "Authorization") String token,
                                                         @RequestBody @Validated UpdateParentNicknameDto updateParentNicknameDto) throws Exception {

        commonMemberServiceV2.putParentNickname(jwtTokenProvider.getId(token), updateParentNicknameDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "초대코드 재설정")
    @PutMapping(value = "/user/v2/commonMember/setting/referralCode")
    public ResponseEntity<ResponseDto> putSettingReferralCode(@RequestHeader(name = "Authorization") String token,
                                                              @RequestBody @Validated UpdateSettingReferralCodeDto updateSettingReferralCodeDto) throws Exception {
        commonMemberServiceV2.putSettingreferralCode(jwtTokenProvider.getId(token), updateSettingReferralCodeDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}