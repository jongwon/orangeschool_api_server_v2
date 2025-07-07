package com.orangeschool.orangeschoolapiserver.domain.schoolSchedule;

import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.schoolSchedule.dto.CreateSchoolScheduleDto;
import com.orangeschool.orangeschoolapiserver.domain.schoolSchedule.dto.SchoolScheduleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "학교일정편집", description = "schoolSchedule")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class SchoolScheduleController {

    private final SchoolScheduleService schoolScheduleService;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "등록, 수정")
    @PostMapping(value = "/user/schoolSchedule")
    public ResponseEntity<ResponseDto> create(@RequestBody @Validated CreateSchoolScheduleDto createSchoolScheduleDto) throws Exception {

        schoolScheduleService.create(createSchoolScheduleDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/schoolSchedule/{commonMemberId}")
    public ResponseEntity<ResponseDto> get(@PathVariable(required = true) Long commonMemberId) throws Exception {

        List<SchoolScheduleDto> schoolScheduleDtoList = schoolScheduleService.get(commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(schoolScheduleDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
