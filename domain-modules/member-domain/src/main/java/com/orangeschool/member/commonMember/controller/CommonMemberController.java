package com.orangeschool.member.commonMember.controller;

import com.orangeschool.auth.dto.request.LoginDto;
import com.orangeschool.auth.dto.request.SocialLoginDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.auth.dto.response.AuthDto;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.member.commonMember.dto.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@Tag(name = "회원", description = "commonMember")
//@RequestMapping("/api")
//@RequiredArgsConstructor
//@RestController
public class CommonMemberController {

//    private final CommonMemberService commonMemberService;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    // create
//    @Operation(summary = "회원가입")
//    @PostMapping(value = "/common/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseDto> create(@ParameterObject CreateCommonMemberDto createCommonMemberDto,
//                                              @RequestPart(required = false) MultipartFile file) throws Exception {
//
//        Long commonMemberId = commonMemberService.create(createCommonMemberDto, file);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.CREATE.getMessage())
//                .data(commonMemberId)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "이메일 중복 체크")
//    @PostMapping(value = "/common/check/email")
//    public ResponseEntity<ResponseDto> checkEmail(@RequestBody @Validated CheckEmailDto checkEmailDto) throws Exception {
//
//        commonMemberService.checkEmail(checkEmailDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "닉네임 중복 체크")
//    @PostMapping(value = "/common/check/nickname")
//    public ResponseEntity<ResponseDto> checkNickName(@RequestBody @Validated CheckNickNameDto checkNickNameDto) throws Exception {
//
//        commonMemberService.checkNickName(checkNickNameDto.getParentNickName());
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "sms 인증 요청")
//    @PostMapping(value = "/common/check/phoneNumber")
//    public ResponseEntity<ResponseDto> checkPhoneNumber(@RequestBody @Validated CheckPhoneNumberDto checkPhoneNumberDto) throws Exception {
//
//        RandomNumberDto randomNumberDto = commonMemberService.checkPhoneNumber(checkPhoneNumberDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data(randomNumberDto)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "sms 인증 요청 - 이메일찾기")
//    @PostMapping(value = "/common/find/email/check/phoneNumber")
//    public ResponseEntity<ResponseDto> findEmailCheckPhoneNumber(@RequestBody @Validated CheckPhoneNumberDto checkPhoneNumberDto) throws Exception {
//
//        RandomNumberDto randomNumberDto = commonMemberService.findEmailCheckPhoneNumber(checkPhoneNumberDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data(randomNumberDto)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "로그인")
//    @PostMapping(value = "/common/login")
//    public ResponseEntity<ResponseDto> login(@RequestBody @Validated LoginDto loginDto) throws Exception {
//
//        AuthDto authDto = commonMemberService.login(loginDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data(authDto)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "소셜 로그인")
//    @PostMapping(value = "/common/login/social")
//    public ResponseEntity<ResponseDto> loginSocial(@RequestBody @Validated SocialLoginDto socialLoginDto) throws Exception {
//
//        AuthDto authDto = commonMemberService.loginSocial(socialLoginDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data(authDto)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "이메일 찾기")
//    @GetMapping(value = "/common/find/email")
//    public ResponseEntity<ResponseDto> findEmail(@ModelAttribute @Validated FindEmailRequestDto findEmailRequestDto) throws Exception {
//
//        FindEmailResponseDto findEmailResponseDto = commonMemberService.findEmail(findEmailRequestDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data(findEmailResponseDto)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "비밀번호 찾기")
//    @GetMapping(value = "/common/find/password")
//    public ResponseEntity<ResponseDto> findPassword(@ModelAttribute @Validated FindPasswordRequestDto findPasswordRequestDto) throws Exception {
//
//        commonMemberService.findPassword(findPasswordRequestDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "비밀번호 재설정")
//    @PostMapping(value = "/common/reset/password")
//    public ResponseEntity<ResponseDto> resetPassword(@RequestBody @Validated ResetPasswordRequestDto resetPasswordRequestDto) throws Exception {
//
//        commonMemberService.resetPassword(resetPasswordRequestDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.SUCCESS.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    // get
//    @Operation(summary = "자녀 목록 조회")
//    @GetMapping(value = "/user/commonMembers")
//    public ResponseEntity<ResponseDto> getChildListToUser(@RequestHeader(name = "Authorization") String token) throws Exception {
//
//        List<CommonMemberDto> commonMemberDtoList = commonMemberService.getChildListToUser(jwtTokenProvider.getId(token));
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(commonMemberDtoList)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "목록 조회")
//    @GetMapping(value = "/admin/commonMembers")
//    public ResponseEntity<ResponseDto> get(
//            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
//            @ModelAttribute CommonMemberFilterDto commonMemberFilter) throws Exception {
//
//        Page<CommonMemberDto> commonMemberDtoPage = commonMemberService.get(pageable, commonMemberFilter);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(commonMemberDtoPage)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "단일 조회")
//    @GetMapping(value = "/admin/commonMember/{commonMemberId}")
//    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long commonMemberId) throws Exception {
//
//        CommonMemberDto commonMemberDto = commonMemberService.getById(commonMemberId);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(commonMemberDto)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "자녀 목록 조회")
//    @GetMapping(value = "/admin/commonMembers/{commonMemberId}")
//    public ResponseEntity<ResponseDto> getChildList(@PathVariable(required = true) Long commonMemberId,
//                                                    @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
//
//        Page<CommonMemberDto> commonMemberDtoPage = commonMemberService.getChildList(commonMemberId, pageable);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(commonMemberDtoPage)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "본인 정보 조회")
//    @GetMapping(value = "/user/commonMember")
//    public ResponseEntity<ResponseDto> getMyInfo(@RequestHeader(name = "Authorization") String token) throws Exception {
//
//        CommonMemberDto commonMemberDto = commonMemberService.getMyInfo(jwtTokenProvider.getId(token));
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(commonMemberDto)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "자녀 정보 조회")
//    @GetMapping(value = "/user/commonMember/{commonMemberId}")
//    public ResponseEntity<ResponseDto> getChildInfo(@RequestHeader(name = "Authorization") String token,
//                                                    @PathVariable(required = true) Long commonMemberId) throws Exception {
//
//        CommonMemberDto commonMemberDto = commonMemberService.getChildInfo(jwtTokenProvider.getId(token), commonMemberId);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(commonMemberDto)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    // put
//    @Operation(summary = "수정")
//    @PutMapping(value = "/admin/commonMember/{commonMemberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long commonMemberId,
//                                           @RequestPart(required = false) MultipartFile file,
//                                           @ParameterObject UpdateCommonMemberDto updateCommonMemberDto) throws Exception {
//
//        commonMemberService.put(commonMemberId, updateCommonMemberDto, file);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "회원 수정")
//    @PutMapping(value = "/user/commonMember", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseDto> putMyInfo(@RequestHeader(name = "Authorization") String token,
//                                                 @RequestPart(required = false) MultipartFile file,
//                                                 @ParameterObject UpdateCommonMemberDtoForApp updateCommonMemberDtoForApp) throws Exception {
//
//        commonMemberService.putMyInfo(jwtTokenProvider.getId(token), updateCommonMemberDtoForApp, file);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "자녀 회원 수정")
//    @PutMapping(value = "/user/commonMember/{commonMemberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseDto> putChild(@RequestHeader(name = "Authorization") String token,
//                                                @PathVariable(required = true) Long commonMemberId,
//                                                @RequestPart(required = false) MultipartFile file,
//                                                @ParameterObject UpdateCommonMemberDtoForApp updateCommonMemberDtoForApp) throws Exception {
//
//        commonMemberService.putChild(jwtTokenProvider.getId(token), commonMemberId, updateCommonMemberDtoForApp, file);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "자녀 학교 수정")
//    @PutMapping(value = "/user/commonMember/school/{commonMemberId}")
//    public ResponseEntity<ResponseDto> putChild(@RequestHeader(name = "Authorization") String token,
//                                                @PathVariable(required = true) Long commonMemberId,
//                                                @RequestBody @Validated UpdateSchoolInfoDto updateSchoolInfoDto) throws Exception {
//
//        commonMemberService.putChildSchoolInfo(jwtTokenProvider.getId(token), commonMemberId, updateSchoolInfoDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "활성상태 수정")
//    @PutMapping(value = "/admin/commonMember/activation/{commonMemberId}")
//    public ResponseEntity<ResponseDto> putActivation(@PathVariable(required = true) Long commonMemberId,
//                                                     @RequestBody UpdateActivationDto updateActivationDto) throws Exception {
//
//        commonMemberService.putActivation(commonMemberId, updateActivationDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "회원 알림 설정")
//    @PutMapping(value = "/user/commonMember/setting/alarm")
//    public ResponseEntity<ResponseDto> putSettingAlarm(@RequestHeader(name = "Authorization") String token,
//                                                       @RequestBody @Validated UpdateSettingAlarmDto updateSettingAlarmDto) throws Exception {
//
//        commonMemberService.putSettingAlarm(jwtTokenProvider.getId(token), updateSettingAlarmDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    // delete
//    @Operation(summary = "삭제")
//    @DeleteMapping(value = "/admin/commonMember/{commonMemberId}")
//    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long commonMemberId) throws Exception {
//
//        commonMemberService.delete(commonMemberId);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.DELETE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "자녀 삭제")
//    @DeleteMapping(value = "/user/child/{commonMemberId}")
//    public ResponseEntity<ResponseDto> deleteChild(@RequestHeader(name = "Authorization") String token,
//                                              @PathVariable(required = true) Long commonMemberId) throws Exception {
//
//        commonMemberService.deleteChild(jwtTokenProvider.getId(token), commonMemberId);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.DELETE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
}