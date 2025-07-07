package com.orangeschool.orangeschoolapiserver.domain.commonMember;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Tag(name = "회원", description = "commonMember")
//@RequestMapping("/api")
//@RequiredArgsConstructor
//@RestController
public class CommonMemberControllerV2 {

//    private final CommonMemberServiceV2 commonMemberServiceV2;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Operation(summary = "회원 우리 동네 설정")
//    @PutMapping(value = "/user/v2/commonMember/setting/region")
//    public ResponseEntity<ResponseDto> putSettingRegion(@RequestHeader(name = "Authorization") String token,
//                                                        @RequestBody @Validated UpdateSettingRegionDto updateSettingRegionDto) throws Exception {
//
//        commonMemberServiceV2.putSettingRegion(jwtTokenProvider.getId(token), updateSettingRegionDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "회원 닉네임 수정")
//    @PutMapping(value = "/user/v2/commonMember")
//    public ResponseEntity<ResponseDto> putParentNickname(@RequestHeader(name = "Authorization") String token,
//                                                         @RequestBody @Validated UpdateParentNicknameDto updateParentNicknameDto) throws Exception {
//
//        commonMemberServiceV2.putParentNickname(jwtTokenProvider.getId(token), updateParentNicknameDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "구성원 요청 목록 조회")
//    @GetMapping(value = "/user/v2/referral/request")
//    public ResponseEntity<ResponseDto> getReferralRequest(@RequestHeader(name = "Authorization") String token) throws Exception {
//
//        List<CommonMemberDto> commonMemberDtoList = commonMemberServiceV2.getReferralRequest(jwtTokenProvider.getId(token));
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(commonMemberDtoList)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "구성원 요청 목록 조회")
//    @GetMapping(value = "/admin/v2/commonMember/family/{commonMemberId}")
//    public ResponseEntity<ResponseDto> getFamily(@PathVariable(required = true) Long commonMemberId) throws Exception {
//
//        List<CommonMemberDto> commonMemberDtoList = commonMemberServiceV2.getReferralRequest(commonMemberId);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(commonMemberDtoList)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "구성원 승인or반려or삭제")
//    @PostMapping(value = "/user/v2/referral/request/confirm/{commonMemberId}")
//    public ResponseEntity<ResponseDto> postReferralRequestConfirm(@RequestHeader(name = "Authorization") String token,
//                                                                  @PathVariable(required = true) Long commonMemberId,
//                                                                  @RequestBody @Validated UpdateReferralConfirmDto updateReferralConfirmDto) throws Exception {
//
//        commonMemberServiceV2.postReferralRequestConfirm(jwtTokenProvider.getId(token), commonMemberId, updateReferralConfirmDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "초대코드 있는지 + 구성원 3명 초과했는지 체크")
//    @PostMapping(value = "/common/check/referralCode")
//    public ResponseEntity<ResponseDto> checkReferralCode(@RequestBody @Validated CheckReferralCodeDto checkReferralCodeDto) throws Exception {
//
//        commonMemberServiceV2.checkReferralCode(checkReferralCodeDto.getReferralCode());
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "초대코드 재설정")
//    @PutMapping(value = "/user/v2/commonMember/setting/referralCode")
//    public ResponseEntity<ResponseDto> putSettingreferralCode(@RequestHeader(name = "Authorization") String token,
//                                                              @RequestBody @Validated UpdateSettingReferralCodeDto updateSettingReferralCodeDto) throws Exception {
//        commonMemberServiceV2.putSettingreferralCode(jwtTokenProvider.getId(token), updateSettingReferralCodeDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "학교 시간 편집")
//    @PutMapping(value = "/user/v2/commonMember/setting/timetable")
//    public ResponseEntity<ResponseDto> putSettingTimetable(@RequestBody @Validated UpdateSettingTimetableDto updateSettingTimetableDto) throws Exception {
//
//        commonMemberServiceV2.putSettingTimetable(updateSettingTimetableDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "학교 시간 편집 조회")
//    @GetMapping(value = "/user/v2/timetable/{commonMemberId}")
//    public ResponseEntity<ResponseDto> getTimeTable(@PathVariable(required = true) Long commonMemberId) throws Exception {
//
//        String timetable = commonMemberServiceV2.getTimeTable(commonMemberId);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(timetable)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
}