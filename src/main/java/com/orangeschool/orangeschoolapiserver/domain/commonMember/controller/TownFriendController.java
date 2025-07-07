package com.orangeschool.orangeschoolapiserver.domain.commonMember.controller;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.TownFriendService;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberProfileDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.TownFriendFilterDto;
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

import java.util.List;

@Tag(name = "동네 친구들 챌린지", description = "townFriend")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class TownFriendController {

    private final TownFriendService townFriendService;
    private final JwtTokenProvider jwtTokenProvider;

    // create

    // get
    @Operation(summary = "내 프로필")
    @GetMapping(value = "/user/my/profile/{commonMemberId}")
    public ResponseEntity<ResponseDto> getProfile(@PathVariable(required = true) Long commonMemberId) throws Exception {

        CommonMemberProfileDto commonMemberProfileDto = townFriendService.getProfile(commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberProfileDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "친구 챌린지 목록")
    @GetMapping(value = "/user/townFriends/{commonMemberId}")
    public ResponseEntity<ResponseDto> get(@PathVariable(required = true) Long commonMemberId,
                                           @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<CommonMemberProfileDto> commonMemberProfileDtoPage = townFriendService.get(commonMemberId, pageable);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberProfileDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "최다 오렌지 보유 친구 목록")
    @GetMapping(value = "/user/townFriends/top/{commonMemberId}")
    public ResponseEntity<ResponseDto> getTop(@PathVariable(required = true) Long commonMemberId) throws Exception {

        List<CommonMemberProfileDto> commonMemberProfileDtoList = townFriendService.getTop(commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberProfileDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "동내 친구들 검색")
    @GetMapping(value = "/user/search/townFriends/{commonMemberId}")
    public ResponseEntity<ResponseDto> getBySearch(@PathVariable(required = true) Long commonMemberId,
                                                   @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                   @ModelAttribute TownFriendFilterDto townFriendFilterDto) throws Exception {

        Page<CommonMemberProfileDto> commonMemberProfileDtoPage = townFriendService.getBySearch(commonMemberId, pageable, townFriendFilterDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberProfileDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "프로필조회")
    @GetMapping(value = "/user/townFriend/{childId}/{commonMemberId}")
    public ResponseEntity<ResponseDto> getById(@RequestHeader(name = "Authorization") String token,
                                               @PathVariable(required = true) Long childId,
                                               @PathVariable(required = true) Long commonMemberId) throws Exception {

        CommonMemberProfileDto commonMemberProfileDto = townFriendService.getById(jwtTokenProvider.getId(token), childId, commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(commonMemberProfileDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // put


    // delete

}