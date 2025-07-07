package com.orangeschool.community.story.comment;


import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.community.story.comment.dto.CreateStoryCommentDto;
import com.orangeschool.community.story.comment.dto.StoryCommentDto;
import com.orangeschool.community.story.comment.dto.StoryCommentSearchDto;
import com.orangeschool.community.story.comment.dto.UpdateStoryCommentDto;
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

@Tag(name = "이야기 댓글", description = "storyComment")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class StoryCommentController {

    private final StoryCommentService storyCommentService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "등록")
    @PostMapping(value = "/user/storyComment/{storyId}")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable(required = true) Long storyId,
                                              @RequestBody @Validated CreateStoryCommentDto createStoryCommentDto) throws Exception {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String principal = (String) authentication.getPrincipal();

        storyCommentService.create(Long.parseLong(principal), storyId, createStoryCommentDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/storyComments/{storyId}")
    public ResponseEntity<ResponseDto> getToUser(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                 @PathVariable(required = true) Long storyId,
                                                 @ModelAttribute StoryCommentSearchDto storyCommentSearchDto) throws Exception {

        Page<StoryCommentDto> storyCommentDtoPage = storyCommentService.get(pageable, storyId, storyCommentSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyCommentDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/storyComment/{storyCommentId}")
    public ResponseEntity<ResponseDto> getByIdToUser(@PathVariable(required = true) Long storyCommentId) throws Exception {

        StoryCommentDto storyCommentDto = storyCommentService.getById(storyCommentId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyCommentDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "수정")
    @PutMapping(value = "/user/storyComment/{storyCommentId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long storyCommentId,
                                           @RequestBody @Validated UpdateStoryCommentDto updateStoryCommentDto) throws Exception {

        storyCommentService.put(storyCommentId, updateStoryCommentDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "삭제")
    @DeleteMapping(value = "/user/storyComment/{storyCommentId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long storyCommentId) throws Exception {

        storyCommentService.delete(storyCommentId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
