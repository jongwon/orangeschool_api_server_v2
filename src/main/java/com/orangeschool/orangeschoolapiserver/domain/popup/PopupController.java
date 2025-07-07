package com.orangeschool.orangeschoolapiserver.domain.popup;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.UpdateActivationDto;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.domain.popup.dto.CreatePopupDto;
import com.orangeschool.orangeschoolapiserver.domain.popup.dto.PopupDto;
import com.orangeschool.orangeschoolapiserver.domain.popup.dto.UpdatePopupDto;
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

@Tag(name = "팝업", description = "popup")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PopupController {

    private final PopupService popupService;

    // create
    @Operation(summary = "등록")
    @PostMapping(value = "/admin/popup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> create(@ParameterObject CreatePopupDto createPopupDto,
                                              @RequestPart(required = true) MultipartFile file) throws Exception {

        popupService.create(createPopupDto, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/popups")
    public ResponseEntity<ResponseDto> get(
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute KeywordSearchDto keywordSearchDto) throws Exception {

        Page<PopupDto> popupDtoPage = popupService.get(pageable, keywordSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(popupDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "목록 조회")
    @GetMapping(value = "/user/popups")
    public ResponseEntity<ResponseDto> getToUser() throws Exception {

        List<PopupDto> popupDtoList = popupService.getToUser();

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(popupDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/popup/{popupId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long popupId) throws Exception {

        PopupDto popupDto = popupService.getById(popupId, false);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(popupDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "팝업 클릭 시")
    @GetMapping(value = "/user/popup/click/{popupId}")
    public ResponseEntity<ResponseDto> getByIdToUser(@PathVariable(required = true) Long popupId) throws Exception {

        PopupDto popupDto = popupService.getById(popupId, true);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(popupDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // put
    @Operation(summary = "수정")
    @PutMapping(value = "/admin/popup/{popupId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long popupId,
                                           @RequestPart(required = false) MultipartFile file,
                                           @ParameterObject UpdatePopupDto updatePopupDto) throws Exception {

        popupService.put(popupId, updatePopupDto, file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "활성상태 수정")
    @PutMapping(value = "/admin/popup/activation/{popupId}")
    public ResponseEntity<ResponseDto> putActivation(@PathVariable(required = true) Long popupId,
                                                     @RequestBody UpdateActivationDto updateActivationDto) throws Exception {

        popupService.putActivation(popupId, updateActivationDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/popup/{popupId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long popupId) throws Exception {

        popupService.delete(popupId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/popups", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> deleteAll(@ModelAttribute IdListDto idListDto) throws Exception {

        popupService.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}