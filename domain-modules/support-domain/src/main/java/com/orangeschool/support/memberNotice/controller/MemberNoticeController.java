package com.orangeschool.support.memberNotice;

import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.support.memberNotice.dto.MemberNoticeDto;
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

@Tag(name = "회원 - 공지사항", description = "memberNotice")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberNoticeController {

    private final MemberNoticeService memberNoticeService;
    private final JwtTokenProvider jwtTokenProvider;

    // create

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/memberNotices")
    public ResponseEntity<ResponseDto> get(@RequestHeader(name = "Authorization") String token,
                                           @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<MemberNoticeDto> memberNoticeDtoPage = memberNoticeService.get(jwtTokenProvider.getId(token), pageable);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(memberNoticeDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/memberNotice/{memberNoticeId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long memberNoticeId) throws Exception {

        MemberNoticeDto memberNoticeDto = memberNoticeService.getById(memberNoticeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(memberNoticeDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update

    // delete
}
