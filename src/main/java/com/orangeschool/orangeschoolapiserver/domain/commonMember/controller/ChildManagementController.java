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

import java.util.List;

@Tag(name = "자녀 관리", description = "child management")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ChildManagementController {

    private final CommonMemberService commonMemberService;
    private final CommonMemberServiceV2 commonMemberServiceV2;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "자녀 목록 조회")
    @GetMapping(value = "/user/commonMembers")
    public ResponseEntity<ResponseDto> getChildListToUser(@RequestHeader(name = "Authorization") String token) throws Exception {

        List<CommonMemberDto> commonMemberDtoList = commonMemberService.getChildListToUser(jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "자녀 정보 조회")
    @GetMapping(value = "/user/commonMember/{commonMemberId}")
    public ResponseEntity<ResponseDto> getChildInfo(@RequestHeader(name = "Authorization") String token,
                                                    @PathVariable(required = true) Long commonMemberId) throws Exception {

        CommonMemberDto commonMemberDto = commonMemberService.getChildInfo(jwtTokenProvider.getId(token), commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "자녀 회원 수정")
    @PutMapping(value = "/user/commonMember/{commonMemberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> putChild(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable(required = true) Long commonMemberId,
                                                @RequestPart(required = false) MultipartFile file,
                                                @ParameterObject UpdateCommonMemberDtoForApp updateCommonMemberDtoForApp) throws Exception {

        commonMemberService.putChild(jwtTokenProvider.getId(token), commonMemberId, updateCommonMemberDtoForApp, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "자녀 학교 수정")
    @PutMapping(value = "/user/commonMember/school/{commonMemberId}")
    public ResponseEntity<ResponseDto> putChildSchoolInfo(@RequestHeader(name = "Authorization") String token,
                                                          @PathVariable(required = true) Long commonMemberId,
                                                          @RequestBody @Validated UpdateSchoolInfoDto updateSchoolInfoDto) throws Exception {

        commonMemberService.putChildSchoolInfo(jwtTokenProvider.getId(token), commonMemberId, updateSchoolInfoDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "자녀 삭제")
    @DeleteMapping(value = "/user/child/{commonMemberId}")
    public ResponseEntity<ResponseDto> deleteChild(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable(required = true) Long commonMemberId) throws Exception {

        commonMemberService.deleteChild(jwtTokenProvider.getId(token), commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "학교 시간 편집")
    @PutMapping(value = "/user/v2/commonMember/setting/timetable")
    public ResponseEntity<ResponseDto> putSettingTimetable(@RequestBody @Validated UpdateSettingTimetableDto updateSettingTimetableDto) throws Exception {

        commonMemberServiceV2.putSettingTimetable(updateSettingTimetableDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "학교 시간 편집 조회")
    @GetMapping(value = "/user/v2/timetable/{commonMemberId}")
    public ResponseEntity<ResponseDto> getTimeTable(@PathVariable(required = true) Long commonMemberId) throws Exception {

        String timetable = commonMemberServiceV2.getTimeTable(commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(timetable)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}