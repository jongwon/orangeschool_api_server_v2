package com.orangeschool.orangeschoolapiserver.domain.academy;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademySearchDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.CreateAcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.UpdateAcademyDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "학원", description = "academy")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AcademyController {

    private final AcademyService academyService;
    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/user/academy")
    public ResponseEntity<ResponseDto> createToUser(@RequestHeader(name = "Authorization") String token,
                                                    @RequestBody @Validated CreateAcademyDto createAcademyDto) throws Exception {

        Long academyId = academyService.createToUser(jwtTokenProvider.getId(token), createAcademyDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data(academyId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "등록")
    @PostMapping(value = "/admin/academy")
    public ResponseEntity<ResponseDto> create(@RequestHeader(name = "Authorization") String token, @RequestBody @Validated CreateAcademyDto createAcademyDto) throws Exception {

        academyService.create(jwtTokenProvider.getId(token), createAcademyDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/academies")
    public ResponseEntity<ResponseDto> getToUser(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                 @ModelAttribute AcademySearchDto academySearchDto) throws Exception {

        Page<AcademyDto> academyDtoPage = academyService.get(pageable, academySearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(academyDtoPage)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/academies")
    public ResponseEntity<ResponseDto> get(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           @ModelAttribute AcademySearchDto academySearchDto) throws Exception {

        Page<AcademyDto> academyDtoPage = academyService.get(pageable, academySearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(academyDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/academy/{academyId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long academyId) throws Exception {

        AcademyDto academyDto = academyService.getById(academyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(academyDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update
    @Operation(summary = "수정")
    @PutMapping(value = "/admin/academy/{academyId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long academyId,
                                           @RequestBody @Validated UpdateAcademyDto updateAcademyDto) throws Exception {

        academyService.put(academyId, updateAcademyDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/academy/{academyId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long academyId) throws Exception {

        academyService.delete(academyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/academies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> delete(@ModelAttribute IdListDto idListDto) throws Exception {

        academyService.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
