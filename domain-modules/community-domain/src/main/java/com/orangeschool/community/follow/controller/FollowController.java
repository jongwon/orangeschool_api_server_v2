package com.orangeschool.community.follow;

import com.orangeschool.community.follow.service.FollowService;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "팔로우", description = "follow")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;
    

    // create
    @Operation(summary = "친구 추가/취소")
    @PostMapping(value = "/user/follow/{myId}/{followerMemberId}")
    public ResponseEntity<ResponseDto> follow(@PathVariable(required = true) Long myId,
                                              @PathVariable(required = true) Long followerMemberId) throws Exception {

        followService.follow(myId, followerMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get

    // put

    // delete

}
