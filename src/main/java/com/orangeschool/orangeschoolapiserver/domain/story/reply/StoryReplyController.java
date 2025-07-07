package com.orangeschool.orangeschoolapiserver.domain.story.reply;


import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.CreateStoryReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.StoryReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.StoryReplySearchDto;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.UpdateStoryReplyDto;
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

@Tag(name = "이야기 답글", description = "storyReply")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class StoryReplyController {

    private final StoryReplyService storyReplyService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "등록")
    @PostMapping(value = "/user/storyReply/{storyCommentId}")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable(required = true) Long storyCommentId,
                                              @RequestBody @Validated CreateStoryReplyDto createStoryReplyDto) throws Exception {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String principal = (String) authentication.getPrincipal();

        storyReplyService.create(Long.parseLong(principal), storyCommentId, createStoryReplyDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/storyReplys/{storyCommentId}")
    public ResponseEntity<ResponseDto> getToUser(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                 @PathVariable(required = true) Long storyCommentId,
                                                 @ModelAttribute StoryReplySearchDto storyReplySearchDto) throws Exception {

        Page<StoryReplyDto> storyReplyDtoPage = storyReplyService.get(pageable, storyCommentId, storyReplySearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyReplyDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/storyReply/{storyReplyId}")
    public ResponseEntity<ResponseDto> getByIdToUser(@PathVariable(required = true) Long storyReplyId) throws Exception {

        StoryReplyDto storyReplyDto = storyReplyService.getById(storyReplyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyReplyDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "수정")
    @PutMapping(value = "/user/storyReply/{storyReplyId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long storyReplyId,
                                           @RequestBody @Validated UpdateStoryReplyDto updateStoryReplyDto) throws Exception {

        storyReplyService.put(storyReplyId, updateStoryReplyDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "삭제")
    @DeleteMapping(value = "/user/storyReply/{storyReplyId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long storyReplyId) throws Exception {

        storyReplyService.delete(storyReplyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
