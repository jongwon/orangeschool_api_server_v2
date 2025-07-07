package com.orangeschool.orangeschoolapiserver.domain.calendar;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.CalendarDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.CalendarSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.GraphDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "달력", description = "calendar")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CalendarController {

    private final CalendarService calendarService;
    private final JwtTokenProvider jwtTokenProvider;

    // create

    // get
    @Operation(summary = "월간 목록 조회")
    @GetMapping(value = "/user/month/calendars")
    public ResponseEntity<ResponseDto> getMonth(@RequestHeader(name = "Authorization") String token,
                                                @ModelAttribute CalendarSearchDto calendarSearchDto) throws Exception {

        Map<LocalDate, List<CalendarDto>> calendarMap = calendarService.getMonth(jwtTokenProvider.getId(token), calendarSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(calendarMap)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "주간 목록 조회")
    @GetMapping(value = "/user/week/calendars")
    public ResponseEntity<ResponseDto> getWeek(@RequestHeader(name = "Authorization") String token,
                                               @ModelAttribute CalendarSearchDto calendarSearchDto) throws Exception {

        List<CalendarDto> calendarDtoList = calendarService.getWeek(jwtTokenProvider.getId(token), calendarSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(calendarDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "주간 목록 조회 (종일 일정만)")
    @GetMapping(value = "/user/week/calendars/allDay")
    public ResponseEntity<ResponseDto> getWeekByIsAllDay(@RequestHeader(name = "Authorization") String token,
                                                         @ModelAttribute CalendarSearchDto calendarSearchDto) throws Exception {

        List<CalendarDto> calendarDtoList = calendarService.getWeekByIsAllDay(jwtTokenProvider.getId(token), calendarSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(calendarDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일간 목록 조회")
    @GetMapping(value = "/user/day/calendars")
    public ResponseEntity<ResponseDto> getDay(@RequestHeader(name = "Authorization") String token,
                                              @ModelAttribute CalendarSearchDto calendarSearchDto) throws Exception {

        List<CalendarDto> calendarDtoList = calendarService.getDay(jwtTokenProvider.getId(token), calendarSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(calendarDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일간 시간표 데이터 조회")
    @GetMapping(value = "/user/timeTable")
    public ResponseEntity<ResponseDto> getTimeTable(@ModelAttribute CalendarSearchDto calendarSearchDto) throws Exception {

        List<CalendarDto> timeTable = calendarService.getTimeTable(calendarSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(timeTable)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "지출 목록 조회")
    @GetMapping(value = "/user/payment")
    public ResponseEntity<ResponseDto> getPayment(@RequestHeader(name = "Authorization") String token,
                                                  @ModelAttribute CalendarSearchDto calendarSearchDto) throws Exception {

        PaymentResponseDto paymentResponseDto = calendarService.getPayment(jwtTokenProvider.getId(token), calendarSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(paymentResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "지출 그래프 데이터 조회")
    @GetMapping(value = "/user/payment/graph")
    public ResponseEntity<ResponseDto> getGraphInfo(@RequestHeader(name = "Authorization") String token,
                                                    @ModelAttribute CalendarSearchDto calendarSearchDto) throws Exception {

        Map<String, List<GraphDto>> graphInfo = calendarService.getGraphInfo(jwtTokenProvider.getId(token), calendarSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(graphInfo)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}