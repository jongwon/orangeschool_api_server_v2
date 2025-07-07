package com.orangeschool.orangeschoolapiserver.domain.leaveMember;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.dto.CreateLeaveMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.dto.LeaveMemberDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "탈퇴", description = "leaveMember")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LeaveMemberController {

    private final LeaveMemberService leaveMemberService;

    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "회원 탈퇴")
    @PostMapping(value = "/user/leaveMember")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @RequestBody @Validated CreateLeaveMemberDto createLeaveMemberDto)
            throws Exception {

        leaveMemberService.create(jwtTokenProvider.getId(token), createLeaveMemberDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/leaveMembers")
    public ResponseEntity<ResponseDto> get(
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute KeywordSearchDto keywordSearchDto) throws Exception {

        Page<LeaveMemberDto> leaveMemberDtoPage = leaveMemberService.get(pageable, keywordSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(leaveMemberDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/leaveMember/{leaveMemberId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long leaveMemberId) throws Exception {

        LeaveMemberDto leaveMemberDto = leaveMemberService.getById(leaveMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(leaveMemberDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/leaveMember/{leaveMemberId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long leaveMemberId) throws Exception {

        leaveMemberService.delete(leaveMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
