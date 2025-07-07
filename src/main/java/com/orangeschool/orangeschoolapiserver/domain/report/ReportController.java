package com.orangeschool.orangeschoolapiserver.domain.report;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.report.dto.CreateReportDto;
import com.orangeschool.orangeschoolapiserver.domain.report.dto.ReportDto;
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

@Tag(name = "신고", description = "report")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportService reportService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "등록")
    @PostMapping(value = "/user/report/{reportedMemberId}")
    public ResponseEntity<ResponseDto> create(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(required = true) Long reportedMemberId,
            @RequestBody CreateReportDto createReportDto
    ) throws Exception {

        reportService.create(jwtTokenProvider.getId(token), reportedMemberId, createReportDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/reports")
    public ResponseEntity<ResponseDto> get(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           @ModelAttribute KeywordSearchDto keywordSearchDto) throws Exception {

        Page<ReportDto> reportDtoPage = reportService.get(pageable, keywordSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(reportDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/report/{reportId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long reportId) throws Exception {

        ReportDto reportDto = reportService.getById(reportId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(reportDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/report/{reportId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long reportId) throws Exception {

        reportService.delete(reportId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}