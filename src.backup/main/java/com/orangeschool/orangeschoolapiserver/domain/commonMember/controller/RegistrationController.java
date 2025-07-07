package com.orangeschool.orangeschoolapiserver.domain.commonMember.controller;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
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

@Tag(name = "회원가입", description = "registration")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class RegistrationController {

    private final CommonMemberService commonMemberService;
    private final CommonMemberServiceV2 commonMemberServiceV2;

    @Operation(summary = "회원가입")
    @PostMapping(value = "/common/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> create(@ParameterObject CreateCommonMemberDto createCommonMemberDto,
                                              @RequestPart(required = false) MultipartFile file) throws Exception {

        Long commonMemberId = commonMemberService.create(createCommonMemberDto, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data(commonMemberId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "이메일 중복 체크")
    @PostMapping(value = "/common/check/email")
    public ResponseEntity<ResponseDto> checkEmail(@RequestBody @Validated CheckEmailDto checkEmailDto) throws Exception {

        commonMemberService.checkEmail(checkEmailDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "닉네임 중복 체크")
    @PostMapping(value = "/common/check/nickname")
    public ResponseEntity<ResponseDto> checkNickName(@RequestBody @Validated CheckNickNameDto checkNickNameDto) throws Exception {

        commonMemberService.checkNickName(checkNickNameDto.getParentNickName());

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "sms 인증 요청")
    @PostMapping(value = "/common/check/phoneNumber")
    public ResponseEntity<ResponseDto> checkPhoneNumber(@RequestBody @Validated CheckPhoneNumberDto checkPhoneNumberDto) throws Exception {

        RandomNumberDto randomNumberDto = commonMemberService.checkPhoneNumber(checkPhoneNumberDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(randomNumberDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "sms 인증 요청 - 이메일찾기")
    @PostMapping(value = "/common/find/email/check/phoneNumber")
    public ResponseEntity<ResponseDto> findEmailCheckPhoneNumber(@RequestBody @Validated CheckPhoneNumberDto checkPhoneNumberDto) throws Exception {

        RandomNumberDto randomNumberDto = commonMemberService.findEmailCheckPhoneNumber(checkPhoneNumberDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(randomNumberDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "초대코드 있는지 + 구성원 3명 초과했는지 체크")
    @PostMapping(value = "/common/check/referralCode")
    public ResponseEntity<ResponseDto> checkReferralCode(@RequestBody @Validated CheckReferralCodeDto checkReferralCodeDto) throws Exception {

        commonMemberServiceV2.checkReferralCode(checkReferralCodeDto.getReferralCode());

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}