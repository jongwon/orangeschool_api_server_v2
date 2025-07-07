package com.orangeschool.community.story.like;


import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.community.story.like.dto.StoryLikeDto;
import com.orangeschool.community.story.like.dto.StoryLikeSearchDto;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "이야기 좋아요", description = "storyLike")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class StoryLikeController {

    private final StoryLikeService storyLikeService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "등록")
    @PostMapping(value = "/user/storyLike/{storyId}")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable(required = true) Long storyId) throws Exception {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String principal = (String) authentication.getPrincipal();

        storyLikeService.create(Long.parseLong(principal), storyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/storyLikes/{storyId}")
    public ResponseEntity<ResponseDto> getToUser(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                 @PathVariable(required = true) Long storyId,
                                                 @ModelAttribute StoryLikeSearchDto storyLikeSearchDto) throws Exception {

        Page<StoryLikeDto> storyLikeDtoPage = storyLikeService.get(pageable, storyId, storyLikeSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyLikeDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
