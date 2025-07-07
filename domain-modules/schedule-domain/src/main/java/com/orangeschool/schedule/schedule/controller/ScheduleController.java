package com.orangeschool.schedule.schedule;

import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.schedule.schedule.dto.*;
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
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "일정", description = "schedule")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/user/schedule")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @RequestBody @Validated CreateScheduleDto createScheduleDto) throws Exception {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String principal = (String) authentication.getPrincipal();

        Long scheduleId = scheduleService.create(createScheduleDto, Long.parseLong(principal));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data(scheduleId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "지출 등록")
    @PostMapping(value = "/user/payment")
    public ResponseEntity<ResponseDto> createPayment(@RequestBody @Validated CreateSchedulePaymentDto createSchedulePaymentDto) throws Exception {

        Long scheduleId = scheduleService.createPayment(createSchedulePaymentDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data(scheduleId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "시간표 메모 등록")
    @PostMapping(value = "/user/timeTable/message/{commonMemberId}")
    public ResponseEntity<ResponseDto> createDayMessage(@PathVariable(required = true) Long commonMemberId,
                                                        @RequestBody @Validated CreateDayMessageDto createDayMessageDto) throws Exception {

        scheduleService.createDayMessage(commonMemberId, createDayMessageDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "시간표 할일 등록 및 체크")
    @PostMapping(value = "/user/timeTable/todoList/{commonMemberId}")
    public ResponseEntity<ResponseDto> createDayTodoList(@PathVariable(required = true) Long commonMemberId,
                                                         @RequestBody @Validated CreateTodoListDto createTodoListDto) throws Exception {

        scheduleService.createTodoList(commonMemberId, createTodoListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "시간표 취침시간 등록")
    @PostMapping(value = "/user/timeTable/sleep/{commonMemberId}")
    public ResponseEntity<ResponseDto> createSleep(@PathVariable(required = true) Long commonMemberId,
                                                   @RequestBody @Validated CreateSleepDto createSleepDto) throws Exception {

        scheduleService.createSleep(commonMemberId, createSleepDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
//    @Operation(summary = "주간 목록 조회")
//    @GetMapping(value = "/user/week/schedules")
//    public ResponseEntity<ResponseDto> getWeekSchedules(@ModelAttribute CalendarSearchDto calendarSearchDto) throws Exception {
//
//        List<ScheduleDto> scheduleDtoList = scheduleService.getWeekSchedules(calendarSearchDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.READ.getMessage())
//                .data(scheduleDtoList)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }

    @Operation(summary = "학원 일정 조회")
    @GetMapping(value = "/admin/schedule/{commonMemberId}")
    public ResponseEntity<ResponseDto> getByCommonMemberId(@PathVariable(required = true) Long commonMemberId,
                                                           @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<ScheduleDto> scheduleDtoPage = scheduleService.getByCommonMemberId(pageable, commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(scheduleDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/schedule/{scheduleId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long scheduleId) throws Exception {

        ScheduleDto scheduleDto = scheduleService.getById(scheduleId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(scheduleDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "시간표 메모 조회")
    @GetMapping(value = "/user/timeTable/message/{commonMemberId}")
    public ResponseEntity<ResponseDto> getDayMessage(@PathVariable(required = true) Long commonMemberId,
                                                     @ModelAttribute DayMessageSearchDto dayMessageSearchDto) throws Exception {

        DayMessageDto dayMessageDto = scheduleService.getDayMessage(commonMemberId, dayMessageSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(dayMessageDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "시간표 할일 조회")
    @GetMapping(value = "/user/timeTable/todoList/{commonMemberId}")
    public ResponseEntity<ResponseDto> getTodoList(@PathVariable(required = true) Long commonMemberId,
                                                   @ModelAttribute DayMessageSearchDto dayMessageSearchDto) throws Exception {

        TodoListDto todoListDto = scheduleService.getTodoList(commonMemberId, dayMessageSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(todoListDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "시간표 취침시간 조회")
    @GetMapping(value = "/user/timeTable/sleep/{commonMemberId}")
    public ResponseEntity<ResponseDto> getSleep(@PathVariable(required = true) Long commonMemberId) throws Exception {

        SleepInfoDto sleepInfoDto = scheduleService.getSleep(commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(sleepInfoDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // put
    @Operation(summary = "수정")
    @PutMapping(value = "/user/schedule/{scheduleId}")
    public ResponseEntity<ResponseDto> put(@RequestHeader(name = "Authorization") String token,
                                           @PathVariable(required = true) Long scheduleId,
                                           @RequestBody @Validated UpdateScheduleDto updateScheduleDto) throws Exception {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String principal = (String) authentication.getPrincipal();

        scheduleService.put(scheduleId, updateScheduleDto, Long.parseLong(principal));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "지출 수정")
    @PutMapping(value = "/user/schedule/payment/{scheduleId}")
    public ResponseEntity<ResponseDto> putPayment(@PathVariable(required = true) Long scheduleId,
                                                  @RequestBody @Validated UpdateSchedulePaymentDto updateSchedulePaymentDto) throws Exception {

        scheduleService.putPayment(scheduleId, updateSchedulePaymentDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "달력 한개만 삭제")
    @DeleteMapping(value = "/user/calendar/{calendarId}")
    public ResponseEntity<ResponseDto> deleteCalendar(@PathVariable(required = true) Long calendarId) throws Exception {

        scheduleService.deleteCalendar(calendarId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "기준 날짜 이후 달력만 삭제")
    @DeleteMapping(value = "/user/schedule/after/{scheduleId}")
    public ResponseEntity<ResponseDto> deleteAfterStandardDate(@PathVariable(required = true) Long scheduleId,
                                                               @RequestBody DeleteScheduleDto deleteScheduleDto) throws Exception {

        scheduleService.deleteAfterStandardDate(scheduleId, deleteScheduleDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "연관된 달력 전체 삭제")
    @DeleteMapping(value = "/user/schedule/{scheduleId}")
    public ResponseEntity<ResponseDto> deleteSchedule(@PathVariable(required = true) Long scheduleId) throws Exception {

        scheduleService.deleteSchedule(scheduleId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}