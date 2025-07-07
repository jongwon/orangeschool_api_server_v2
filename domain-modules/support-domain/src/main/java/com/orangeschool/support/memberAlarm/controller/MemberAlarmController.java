package com.orangeschool.support.memberAlarm;

import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.support.memberAlarm.dto.MemberAlarmDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 - 알림", description = "memberAlarm")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberAlarmController {

    private final MemberAlarmService memberAlarmService;
    private final JwtTokenProvider jwtTokenProvider;

    // create

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/memberAlarm")
    public ResponseEntity<ResponseDto> get(@RequestHeader(name = "Authorization") String token,
                                           @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<MemberAlarmDto> memberAlarmDtoPage = memberAlarmService.get(jwtTokenProvider.getId(token), pageable);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(memberAlarmDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/memberAlarm/{memberAlarmId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long memberAlarmId) throws Exception {

        MemberAlarmDto memberAlarmDto = memberAlarmService.getById(memberAlarmId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(memberAlarmDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "새로운 알람 존재 여부확인")
    @GetMapping(value = "/user/memberAlarm/new/exist")
    public ResponseEntity<ResponseDto> getNewExist(@RequestHeader(name = "Authorization") String token) throws Exception {

        Boolean newExist = memberAlarmService.getNewExist(jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(newExist)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update

    // delete
}
