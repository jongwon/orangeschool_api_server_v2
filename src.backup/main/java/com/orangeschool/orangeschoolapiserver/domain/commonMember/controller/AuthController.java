package com.orangeschool.orangeschoolapiserver.domain.commonMember.controller;

import com.orangeschool.orangeschoolapiserver.common.dto.request.LoginDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.SocialLoginDto;
import com.orangeschool.orangeschoolapiserver.common.dto.response.AuthDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.CommonMemberService;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.FindEmailRequestDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.FindEmailResponseDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.FindPasswordRequestDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.ResetPasswordRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "authentication")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final CommonMemberService commonMemberService;

    @Operation(summary = "로그인")
    @PostMapping(value = "/common/login")
    public ResponseEntity<ResponseDto> login(@RequestBody @Validated LoginDto loginDto) throws Exception {

        AuthDto authDto = commonMemberService.login(loginDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(authDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "소셜 로그인")
    @PostMapping(value = "/common/login/social")
    public ResponseEntity<ResponseDto> loginSocial(@RequestBody @Validated SocialLoginDto socialLoginDto) throws Exception {

        AuthDto authDto = commonMemberService.loginSocial(socialLoginDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(authDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "이메일 찾기")
    @GetMapping(value = "/common/find/email")
    public ResponseEntity<ResponseDto> findEmail(@ModelAttribute @Validated FindEmailRequestDto findEmailRequestDto) throws Exception {

        FindEmailResponseDto findEmailResponseDto = commonMemberService.findEmail(findEmailRequestDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(findEmailResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "비밀번호 찾기")
    @GetMapping(value = "/common/find/password")
    public ResponseEntity<ResponseDto> findPassword(@ModelAttribute @Validated FindPasswordRequestDto findPasswordRequestDto) throws Exception {

        commonMemberService.findPassword(findPasswordRequestDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "비밀번호 재설정")
    @PostMapping(value = "/common/reset/password")
    public ResponseEntity<ResponseDto> resetPassword(@RequestBody @Validated ResetPasswordRequestDto resetPasswordRequestDto) throws Exception {

        commonMemberService.resetPassword(resetPasswordRequestDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}