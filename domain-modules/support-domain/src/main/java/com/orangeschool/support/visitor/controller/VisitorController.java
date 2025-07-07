package com.orangeschool.support.visitor;


import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.support.visitor.dto.CreateVisitorDto;
import com.orangeschool.support.visitor.dto.MemberCountDto;
import com.orangeschool.support.visitor.dto.VisitorDto;
import com.orangeschool.support.visitor.dto.VisitorFilterDto;
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

@Tag(name = "방문자", description = "visitor")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class VisitorController {
    private final VisitorService visitorService;

    @Operation(summary = "방문자 등록")
    @PostMapping(value = "/visitor")
    public ResponseEntity<ResponseDto> create(@RequestBody @Validated CreateVisitorDto createVisitorDto) throws Exception {

        visitorService.create(createVisitorDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "방문자 목록 조회")
    @GetMapping(value = "/admin/visitors")
    public ResponseEntity<ResponseDto> get(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           @ParameterObject @ModelAttribute VisitorFilterDto visitorFilterDto) throws Exception {

        Page<VisitorDto> visitorDtoList = visitorService.get(pageable, visitorFilterDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(visitorDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "사용자 카운트 조회")
    @GetMapping(value = "/admin/member/count")
    public ResponseEntity<ResponseDto> getMemberCount() throws Exception {

        MemberCountDto memberCountDto = visitorService.getMemberCount();

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(memberCountDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
