package com.orangeschool.community.story.story;

import com.orangeschool.community.story.story.service.StoryService;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.community.story.story.dto.CreateStoryDto;
import com.orangeschool.community.story.story.dto.StoryDto;
import com.orangeschool.community.story.story.dto.StorySearchDto;
import com.orangeschool.community.story.story.dto.UpdateStoryDto;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "이야기", description = "story")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class StoryController {

    private final StoryService storyService;
    

    @Operation(summary = "등록")
    @PostMapping(value = "/user/story", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @ParameterObject CreateStoryDto createStoryDto,
                                              @RequestPart(required = false) List<MultipartFile> files) throws Exception {

        storyService.create(jwtTokenProvider.getId(token), createStoryDto, files);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/stories")
    public ResponseEntity<ResponseDto> getToUser(@RequestHeader(name = "Authorization") String token,
                                                 @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                 @ModelAttribute StorySearchDto storySearchDto) throws Exception {

        Page<StoryDto> storyDtoPage = storyService.getToUser(pageable, storySearchDto, jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/stories")
    public ResponseEntity<ResponseDto> get(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           @ModelAttribute StorySearchDto storySearchDto) throws Exception {

        Page<StoryDto> storyDtoPage = storyService.get(pageable, storySearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/story/{storyId}")
    public ResponseEntity<ResponseDto> getByIdToUser(@RequestHeader(name = "Authorization") String token,
                                                     @PathVariable(required = true) Long storyId,
                                                     @ModelAttribute StorySearchDto storySearchDto) throws Exception {

        StoryDto storyDto = storyService.getByIdToUser(storyId, jwtTokenProvider.getId(token), storySearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/story/{storyId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long storyId) throws Exception {

        StoryDto storyDto = storyService.getById(storyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(storyDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "수정")
    @PutMapping(value = "/user/story/{storyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long storyId,
                                           @ParameterObject UpdateStoryDto updateStoryDto,
                                           @RequestPart(required = false) List<MultipartFile> files) throws Exception {

        storyService.put(storyId, updateStoryDto, files);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "활성상태 수정")
    @PutMapping(value = "/admin/story/activation/{storyId}")
    public ResponseEntity<ResponseDto> putActivation(@PathVariable(required = true) Long storyId,
                                                     @RequestBody UpdateActivationDto updateActivationDto) throws Exception {

        storyService.putActivation(storyId, updateActivationDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "삭제")
    @DeleteMapping(value = "/user/story/{storyId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long storyId) throws Exception {

        storyService.delete(storyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/stories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> delete(@ModelAttribute IdListDto idListDto) throws Exception {

        storyService.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
