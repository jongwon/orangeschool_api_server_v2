package com.orangeschool.support.manager;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.dto.request.LoginDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.common.dto.response.AuthDto;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.support.manager.dto.CheckPhoneDto;
import com.orangeschool.support.manager.dto.CreateManagerDto;
import com.orangeschool.support.manager.dto.ManagerDto;
import com.orangeschool.support.manager.dto.UpdateManagerDto;
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

@Tag(name = "관리자", description = "manager")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ManagerController {

    private final ManagerService managerService;

    private final JwtTokenProvider jwtTokenProvider;

    // create
    @Operation(summary = "회원가입")
    @PostMapping(value = "/common/manager")
    public ResponseEntity<ResponseDto> create(@RequestBody @Validated CreateManagerDto createManagerDto)
            throws Exception {

        managerService.create(createManagerDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.CREATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "로그인")
    @PostMapping(value = "/common/manager/login")
    public ResponseEntity<ResponseDto> login(@RequestBody @Validated LoginDto loginDto) throws Exception {

        AuthDto authDto = managerService.login(loginDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(authDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "휴대폰인증")
    @PostMapping(value = "/common/manager/check/phone")
    public ResponseEntity<ResponseDto> checkPhone(@RequestBody @Validated CheckPhoneDto checkPhoneDto) throws Exception {

        AuthDto authDto = managerService.checkPhone(checkPhoneDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(authDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // get
    @Operation(summary = "목록 조회")
    @GetMapping(value = "/admin/managers")
    public ResponseEntity<ResponseDto> get(
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute KeywordSearchDto keywordSearchDto) throws Exception {

        Page<ManagerDto> managerDtoPage = managerService.get(pageable, keywordSearchDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(managerDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "단일 조회")
    @GetMapping(value = "/admin/manager/{managerId}")
    public ResponseEntity<ResponseDto> getById(@PathVariable(required = true) Long managerId) throws Exception {

        ManagerDto managerDto = managerService.getById(managerId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(managerDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // put
    @Operation(summary = "수정")
    @PutMapping(value = "/admin/manager/{managerId}")
    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long managerId,
                                           @RequestBody @Validated UpdateManagerDto updateManagerDto) throws Exception {

        managerService.put(managerId, updateManagerDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "활성상태 수정")
    @PutMapping(value = "/admin/manager/activation/{managerId}")
    public ResponseEntity<ResponseDto> putActivation(@PathVariable(required = true) Long managerId,
                                                     @RequestBody UpdateActivationDto updateActivationDto) throws Exception {

        managerService.putActivation(managerId, updateActivationDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.UPDATE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "승인")
    @PutMapping(value = "/admin/manager/approve/{managerId}")
    public ResponseEntity<ResponseDto> putApprove(@PathVariable(required = true) Long managerId) throws Exception {

        managerService.putApprove(managerId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // delete
    @Operation(summary = "삭제")
    @DeleteMapping(value = "/admin/manager/{managerId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long managerId) throws Exception {

        managerService.delete(managerId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 삭제")
    @DeleteMapping(value = "/admin/managers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> deleteAll(@ModelAttribute IdListDto idListDto) throws Exception {

        managerService.deleteAll(idListDto);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.DELETE.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
