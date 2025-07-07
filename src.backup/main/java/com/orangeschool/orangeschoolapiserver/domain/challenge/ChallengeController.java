package com.orangeschool.orangeschoolapiserver.domain.challenge;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.ChallengeDto;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.ChallengeFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.CreateChallengeDto;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.UpdateChallengeDto;
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

@Tag(name = "Challenge", description = "challenge")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ChallengeController {

    private final ChallengeService challengeService;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/user/challenge")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @RequestBody @Validated CreateChallengeDto createChallengeDto) throws Exception {

        challengeService.create(jwtTokenProvider.getId(token), createChallengeDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "도장 요청")
    @PostMapping(value = "/user/challenge/stamp/{challengeId}")
    public ResponseEntity<ResponseDto> challengeStamp(@RequestHeader(name = "Authorization") String token,
                                                      @PathVariable(required = true) Long challengeId) throws Exception {

        challengeService.challengeStamp(jwtTokenProvider.getId(token), challengeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "부모/도장추가")
    @PostMapping(value = "/user/challenge/stamp/add/{challengeId}")
    public ResponseEntity<ResponseDto> addStamp(@PathVariable(required = true) Long challengeId) throws Exception {

        challengeService.challengeAddStamp(challengeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "부모/도장제거")
    @PostMapping(value = "/user/challenge/stamp/remove/{challengeId}")
    public ResponseEntity<ResponseDto> removeStamp(@PathVariable(required = true) Long challengeId) throws Exception {

        challengeService.challengeRemoveStamp(challengeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "완료하기")
    @PostMapping(value = "/user/challenge/complete/{challengeId}")
    public ResponseEntity<ResponseDto> challengeComplete(@PathVariable(required = true) Long challengeId) throws Exception {

        challengeService.challengeComplete(challengeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/challenges/{childId}")
    public ResponseEntity<ResponseDto> get(@RequestHeader(name = "Authorization") String token,
                                           @PathVariable(required = true) Long childId,
                                           @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                           @ModelAttribute ChallengeFilterDto challengeFilterDto) throws Exception {

        Page<ChallengeDto> challengeDtoPage = challengeService.get(jwtTokenProvider.getId(token), childId, pageable, challengeFilterDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(challengeDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/challenge/{challengeId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long challengeId) throws Exception {

        ChallengeDto challengeDto = challengeService.getById(challengeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(challengeDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update
    @Operation(summary = "수정")
    @PutMapping(value = "/user/challenge/{challengeId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long challengeId,
                                           @RequestBody @Validated UpdateChallengeDto updateChallengeDto) throws Exception {

        challengeService.put(challengeId, updateChallengeDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/user/challenge/{challengeId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long challengeId) throws Exception {

        challengeService.delete(challengeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
