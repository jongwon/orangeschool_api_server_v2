package com.orangeschool.orangeschoolapiserver.domain.challengeTemp;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.UpdateChallengeDto;
import com.orangeschool.orangeschoolapiserver.domain.challengeTemp.dto.ChallengeTempDto;
import com.orangeschool.orangeschoolapiserver.domain.challengeTemp.dto.UpdateChallengeConfirmDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Challenge", description = "challenge")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ChallengeTempController {

    private final ChallengeTempService challengeTempService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/challenge/request/{challengeId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long challengeId) throws Exception {

        ChallengeTempDto challengeTempDto = challengeTempService.getById(challengeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(challengeTempDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update
    @Operation(summary = "수정요청")
    @PutMapping(value = "/user/challenge/request/{challengeId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long challengeId,
                                           @RequestBody @Validated UpdateChallengeDto updateChallengeDto) throws Exception {

        challengeTempService.put(challengeId, updateChallengeDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "수정 승인 or 반려")
    @PostMapping(value = "/user/challenge/confirm/{challengeId}")
    public ResponseEntity<ResponseDto> challengeConfirm(
            @PathVariable(required = true) Long challengeId, @RequestBody @Validated UpdateChallengeConfirmDto updateChallengeConfirmDto) throws Exception {

        challengeTempService.challengeConfirm(challengeId, updateChallengeConfirmDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
