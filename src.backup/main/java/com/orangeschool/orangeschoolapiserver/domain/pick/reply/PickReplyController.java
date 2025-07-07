package com.orangeschool.orangeschoolapiserver.domain.pick.reply;


import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.dto.CreatePickReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.dto.PickReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.dto.PickReplySearchDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.dto.UpdatePickReplyDto;
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

@Tag(name = "O's Life 답글", description = "pickReply")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PickReplyController {

    private final PickReplyService pickReplyService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "등록")
    @PostMapping(value = "/user/pickReply/{pickCommentId}")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable(required = true) Long pickCommentId,
                                              @RequestBody @Validated CreatePickReplyDto createPickReplyDto) throws Exception {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String principal = (String) authentication.getPrincipal();

        pickReplyService.create(Long.parseLong(principal), pickCommentId, createPickReplyDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/pickReplys/{pickCommentId}")
    public ResponseEntity<ResponseDto> getToUser(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                 @PathVariable(required = true) Long pickCommentId,
                                                 @ModelAttribute PickReplySearchDto pickReplySearchDto) throws Exception {

        Page<PickReplyDto> pickReplyDtoPage = pickReplyService.get(pageable, pickCommentId, pickReplySearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickReplyDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/pickReply/{pickReplyId}")
    public ResponseEntity<ResponseDto> getByIdToUser(@PathVariable(required = true) Long pickReplyId) throws Exception {

        PickReplyDto pickReplyDto = pickReplyService.getById(pickReplyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickReplyDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "수정")
    @PutMapping(value = "/user/pickReply/{pickReplyId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long pickReplyId,
                                           @RequestBody @Validated UpdatePickReplyDto updatePickReplyDto) throws Exception {

        pickReplyService.put(pickReplyId, updatePickReplyDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "삭제")
    @DeleteMapping(value = "/user/pickReply/{pickReplyId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long pickReplyId) throws Exception {

        pickReplyService.delete(pickReplyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
