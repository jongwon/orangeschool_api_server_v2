package com.orangeschool.orangeschoolapiserver.domain.terms;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.dto.CreateTermsDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.dto.TermsDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.dto.UpdateTermsDto;
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

@Tag(name = "약관", description = "terms")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class TermsController {

    private final TermsService termsService;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/admin/terms")
    public ResponseEntity<ResponseDto> create(@RequestBody @Validated CreateTermsDto createTermsDto) throws Exception {

        termsService.create(createTermsDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/common/terms")
    public ResponseEntity<ResponseDto> get(
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
            throws Exception {

        Page<TermsDto> termsDtoPage = termsService.get(pageable);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(termsDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/common/terms/{termsId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long termsId) throws Exception {

        TermsDto termsDto = termsService.getById(termsId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(termsDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update
    @Operation(summary = "수정")
    @PutMapping(value = "/admin/terms/{termsId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long termsId,
                                           @RequestBody @Validated UpdateTermsDto updateTermsDto) throws Exception {

        termsService.put(termsId, updateTermsDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/terms/{termsId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long termsId) throws Exception {

        termsService.delete(termsId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/terms", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> deleteAll(@ModelAttribute IdListDto idListDto) throws Exception {

        termsService.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
