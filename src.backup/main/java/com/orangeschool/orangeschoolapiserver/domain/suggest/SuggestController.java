package com.orangeschool.orangeschoolapiserver.domain.suggest;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.suggest.dto.CreateSuggestDto;
import com.orangeschool.orangeschoolapiserver.domain.suggest.dto.SuggestDto;
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

@Tag(name = "1:1기능제안", description = "suggest")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class SuggestController {

    private final SuggestService suggestService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "등록")
    @PostMapping(value = "/user/suggest")
    public ResponseEntity<ResponseDto> create(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CreateSuggestDto createSuggestDto
    ) throws Exception {

        suggestService.create(jwtTokenProvider.getId(token), createSuggestDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/suggests")
    public ResponseEntity<ResponseDto> get(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           @ModelAttribute KeywordSearchDto keywordSearchDto) throws Exception {

        Page<SuggestDto> suggestDtoPage = suggestService.get(pageable, keywordSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(suggestDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/suggest/{suggestId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long suggestId) throws Exception {

        SuggestDto suggestDto = suggestService.getById(suggestId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(suggestDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/suggest/{suggestId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long suggestId) throws Exception {

        suggestService.delete(suggestId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}