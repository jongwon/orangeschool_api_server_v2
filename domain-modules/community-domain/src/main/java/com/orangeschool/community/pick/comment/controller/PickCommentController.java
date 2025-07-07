package com.orangeschool.community.pick.comment;


import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.community.pick.comment.dto.CreatePickCommentDto;
import com.orangeschool.community.pick.comment.dto.PickCommentDto;
import com.orangeschool.community.pick.comment.dto.PickCommentSearchDto;
import com.orangeschool.community.pick.comment.dto.UpdatePickCommentDto;
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

@Tag(name = "O's Life 댓글", description = "pickComment")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PickCommentController {

    private final PickCommentService pickCommentService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "등록")
    @PostMapping(value = "/user/pickComment/{pickId}")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable(required = true) Long pickId,
                                              @RequestBody @Validated CreatePickCommentDto createPickCommentDto) throws Exception {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String principal = (String) authentication.getPrincipal();

        pickCommentService.create(Long.parseLong(principal), pickId, createPickCommentDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/pickComments/{pickId}")
    public ResponseEntity<ResponseDto> getToUser(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                 @PathVariable(required = true) Long pickId,
                                                 @ModelAttribute PickCommentSearchDto pickCommentSearchDto) throws Exception {

        Page<PickCommentDto> pickCommentDtoPage = pickCommentService.get(pageable, pickId, pickCommentSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickCommentDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/pickComment/{pickCommentId}")
    public ResponseEntity<ResponseDto> getByIdToUser(@PathVariable(required = true) Long pickCommentId) throws Exception {

        PickCommentDto pickCommentDto = pickCommentService.getById(pickCommentId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickCommentDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "수정")
    @PutMapping(value = "/user/pickComment/{pickCommentId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long pickCommentId,
                                           @RequestBody @Validated UpdatePickCommentDto updatePickCommentDto) throws Exception {

        pickCommentService.put(pickCommentId, updatePickCommentDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "삭제")
    @DeleteMapping(value = "/user/pickComment/{pickCommentId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long pickCommentId) throws Exception {

        pickCommentService.delete(pickCommentId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
