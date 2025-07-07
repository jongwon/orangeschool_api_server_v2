package com.orangeschool.orangeschoolapiserver.domain.alarm;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.AlarmDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.AlarmFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.CreateAlarmDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "알림", description = "alarm")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/admin/alarm")
    public ResponseEntity<ResponseDto> create(@RequestBody @Validated CreateAlarmDto createAlarmDto) throws Exception {

        alarmService.create(createAlarmDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/alarms")
    public ResponseEntity<ResponseDto> get(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           @ModelAttribute AlarmFilterDto alarmFilterDto) throws Exception {

        Page<AlarmDto> alarmDtoList = alarmService.get(pageable, alarmFilterDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(alarmDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/alarm/{alarmId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long alarmId) throws Exception {

        AlarmDto alarmDto = alarmService.getById(alarmId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(alarmDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/alarm/{alarmId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long alarmId) throws Exception {

        alarmService.delete(alarmId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/alarms", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> delete(@ModelAttribute IdListDto idListDto) throws Exception {

        alarmService.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
