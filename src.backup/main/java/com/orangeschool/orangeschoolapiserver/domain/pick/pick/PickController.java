package com.orangeschool.orangeschoolapiserver.domain.pick.pick;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.UpdateActivationDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto.CreatePickDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto.PickDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto.PickFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto.UpdatePickDto;
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

@Tag(name = "O's Life", description = "pick")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PickController {

    private final PickService pickService;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/admin/pick", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token,
                                              @ParameterObject CreatePickDto createPickDto,
                                              @RequestPart(required = false) List<MultipartFile> files) throws Exception {

        pickService.create(jwtTokenProvider.getId(token), createPickDto, files);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/picks")
    public ResponseEntity<ResponseDto> getToUser(@RequestHeader(name = "Authorization") String token,
                                                 @ParameterObject @PageableDefault(size = 10, page = 0, sort = "number", direction = Sort.Direction.ASC) Pageable pageable,
                                                 @ModelAttribute PickFilterDto pickFilterDto) throws Exception {

        Page<PickDto> pickDtoPage = pickService.getToUser(pageable, pickFilterDto, jwtTokenProvider.getId(token));

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/picks")
    public ResponseEntity<ResponseDto> get(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "number", direction = Sort.Direction.ASC) Pageable pageable,
                                           @ModelAttribute PickFilterDto pickFilterDto) throws Exception {

        Page<PickDto> pickDtoPage = pickService.get(pageable, pickFilterDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/pick/{pickId}")
    public ResponseEntity<ResponseDto> getByIdToPublic(@PathVariable(required = true) Long pickId) throws Exception {

        PickDto pickDto = pickService.getByIdToPublic(pickId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/user/pick/{pickId}")
    public ResponseEntity<ResponseDto> getByIdToUser(@RequestHeader(name = "Authorization") String token,
                                                     @PathVariable(required = true) Long pickId,
                                                     @ModelAttribute PickFilterDto pickFilterDto) throws Exception {

        PickDto pickDto = pickService.getByIdToUser(pickId, jwtTokenProvider.getId(token), pickFilterDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/pick/{pickId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long pickId) throws Exception {

        PickDto pickDto = pickService.getById(pickId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(pickDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update
    @Operation(summary = "수정")
    @PutMapping(value = "/admin/pick/{pickId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long pickId,
                                           @ParameterObject UpdatePickDto updatePickDto,
                                           @RequestPart(required = false) List<MultipartFile> files) throws Exception {

        pickService.put(pickId, updatePickDto, files);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "활성상태 수정")
    @PutMapping(value = "/admin/pick/activation/{pickId}")
    public ResponseEntity<ResponseDto> putActivation(@PathVariable(required = true) Long pickId,
                                                     @RequestBody UpdateActivationDto updateActivationDto) throws Exception {

        pickService.putActivation(pickId, updateActivationDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/pick/{pickId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long pickId) throws Exception {

        pickService.delete(pickId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/picks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> delete(@ModelAttribute IdListDto idListDto) throws Exception {

        pickService.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
