package com.orangeschool.support.notice;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.support.notice.dto.CreateNoticeDto;
import com.orangeschool.support.notice.dto.NoticeDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공지사항", description = "notice")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/admin/notice")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @RequestBody @Validated CreateNoticeDto createNoticeDto) throws Exception {

        noticeService.create(jwtTokenProvider.getId(token), createNoticeDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/notices")
    public ResponseEntity<ResponseDto> get(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           @ModelAttribute KeywordSearchDto keywordSearchDto) throws Exception {

        Page<NoticeDto> noticeDtoPage = noticeService.get(pageable, keywordSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(noticeDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/notice/{noticeId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long noticeId) throws Exception {

        NoticeDto noticeDto = noticeService.getById(noticeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(noticeDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/notice/{noticeId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long noticeId) throws Exception {

        noticeService.delete(noticeId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/notices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> delete(@ModelAttribute IdListDto idListDto) throws Exception {

        noticeService.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
