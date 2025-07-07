package com.orangeschool.schedule.scheduleTemp;

import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.schedule.dto.*;
import com.orangeschool.schedule.scheduleTemp.dto.ScheduleTempDto;
import com.orangeschool.schedule.scheduleTemp.dto.UpdateScheduleConfirmDto;
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

@Tag(name = "일정-자녀", description = "scheduleTemp")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ScheduleTempController {

    private final ScheduleTempService scheduleTempService;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "아이-등록요청")
    @PostMapping(value = "/user/schedule/request")
    public ResponseEntity<ResponseDto> create(@RequestBody @Validated CreateScheduleDto createScheduleDto) throws Exception {

        Long scheduleTempId = scheduleTempService.create(createScheduleDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(scheduleTempId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "아이-지출등록요청")
    @PostMapping(value = "/user/schedule/payment/request")
    public ResponseEntity<ResponseDto> createPayment(@RequestBody @Validated CreateSchedulePaymentDto createSchedulePaymentDto) throws Exception {

        Long scheduleTempId = scheduleTempService.createPayment(createSchedulePaymentDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(scheduleTempId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "아이-수정요청")
    @PutMapping(value = "/user/schedule/request/{scheduleId}")
    public ResponseEntity<ResponseDto> put(@RequestHeader(name = "Authorization") String token,
                                           @PathVariable(required = true) Long scheduleId,
                                           @RequestBody @Validated UpdateScheduleDto updateScheduleDto) throws Exception {

        Long scheduleTempId = scheduleTempService.put(jwtTokenProvider.getId(token), scheduleId, updateScheduleDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(scheduleTempId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "아이-지출수정요청")
    @PutMapping(value = "/user/schedule/payment/request/{scheduleId}")
    public ResponseEntity<ResponseDto> putPayment(@RequestHeader(name = "Authorization") String token,
                                                  @PathVariable(required = true) Long scheduleId,
                                                  @RequestBody @Validated UpdateSchedulePaymentDto updateSchedulePaymentDto) throws Exception {

        Long scheduleTempId = scheduleTempService.putPayment(jwtTokenProvider.getId(token), scheduleId, updateSchedulePaymentDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(scheduleTempId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "승인 요청 목록 조회")
    @GetMapping(value = "/user/schedule/request")
    public ResponseEntity<ResponseDto> getToUser(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                 @RequestHeader(name = "Authorization") String token) throws Exception {

        Page<ScheduleTempDto> storyTempDtoPage = scheduleTempService.get(pageable, jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyTempDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "승인 요청 단일 조회")
    @GetMapping(value = "/user/schedule/request/{scheduleTempId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long scheduleTempId) throws Exception {

        ScheduleTempDto scheduleTempDto = scheduleTempService.getById(scheduleTempId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(scheduleTempDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "승인 요청 카운트조회")
    @GetMapping(value = "/user/schedule/request/count")
    public ResponseEntity<ResponseDto> getCount(@RequestHeader(name = "Authorization") String token) throws Exception {

        Long result = scheduleTempService.getCount(jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "승인or반려")
    @PostMapping(value = "/user/schedule/request/confirm/{scheduleTempId}")
    public ResponseEntity<ResponseDto> updateScheduleConfirm(@PathVariable(required = true) Long scheduleTempId,
                                                             @RequestBody @Validated UpdateScheduleConfirmDto updateScheduleConfirmDto) throws Exception {

        scheduleTempService.updateScheduleConfirm(scheduleTempId, updateScheduleConfirmDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}