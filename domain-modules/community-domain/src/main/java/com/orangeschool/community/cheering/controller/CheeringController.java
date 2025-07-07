package com.orangeschool.community.cheering.controller;

import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.community.cheering.CheeringService;
import com.orangeschool.community.cheering.dto.CheeringDto;
import com.orangeschool.community.cheering.dto.CheeringRequestDto;
import com.orangeschool.member.commonMember.dto.CommonMemberProfileDto;
import com.orangeschool.member.commonMember.dto.TownFriendFilterDto;
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

@Tag(name = "응원", description = "cheering")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CheeringController {

    private final CheeringService cheeringService;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "친구 응원/취소")
    @PostMapping(value = "/user/cheering/{cheeredMemberId}")
    public ResponseEntity<ResponseDto> cheering(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable(required = true) Long cheeredMemberId,
                                                @RequestBody @Validated CheeringRequestDto cheeringRequestDto) throws Exception {

        cheeringService.cheering(jwtTokenProvider.getId(token), cheeredMemberId, cheeringRequestDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "응원 목록")
    @GetMapping(value = "/user/cheerings/{cheeredId}")
    public ResponseEntity<ResponseDto> get(@PathVariable(required = true) Long cheeredId,
                                           @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<CheeringDto> cheeringDtoPage = cheeringService.get(cheeredId, pageable);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(cheeringDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // put

    // delete

}