package com.orangeschool.orangeschoolapiserver.domain.pick.like;


import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.pick.like.dto.PickLikeDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.like.dto.PickLikeSearchDto;
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

@Tag(name = "O's Life 좋아요", description = "pickLike")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PickLikeController {

    private final PickLikeService pickLikeService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "등록")
    @PostMapping(value = "/user/pickLike/{pickId}")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable(required = true) Long pickId) throws Exception {

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String principal = (String) authentication.getPrincipal();

        pickLikeService.create(Long.parseLong(principal), pickId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/pickLikes/{pickId}")
    public ResponseEntity<ResponseDto> getToUser(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                 @PathVariable(required = true) Long pickId,
                                                 @ModelAttribute PickLikeSearchDto pickLikeSearchDto) throws Exception {

        Page<PickLikeDto> pickLikeDtoPage = pickLikeService.get(pageable, pickId, pickLikeSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickLikeDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
