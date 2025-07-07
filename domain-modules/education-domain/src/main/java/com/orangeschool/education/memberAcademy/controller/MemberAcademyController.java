package com.orangeschool.education.memberAcademy;

import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.response.ResponseDto;
import com.orangeschool.education.academy.dto.AcademyDto;
import com.orangeschool.member.commonMember.dto.CommonMemberDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 - 학원", description = "memberAcademy")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberAcademyController {

    private final MemberAcademyService memberAcademyService;

    // create
//    @Operation(summary = "등록")
//    @PostMapping(value = "/admin/memberAcademy")
//    public ResponseEntity<ResponseDto> create(@RequestBody @Validated CreateMemberAcademyDto createMemberAcademyDto) throws Exception {
//
//        memberAcademyService.create(createMemberAcademyDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.CREATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }

    // get
    @Operation(summary = "회원 별 학원 목록 조회")
    @GetMapping(value = "/admin/commonMember/memberAcademies/{commonMemberId}")
    public ResponseEntity<ResponseDto> getByCommonMember(@PathVariable(required = true) Long commonMemberId,
                                           @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<AcademyDto> memberAcademyDtoPage = memberAcademyService.getByCommonMember(pageable, commonMemberId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(memberAcademyDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "학원 별 회원 목록 조회")
    @GetMapping(value = "/admin/academy/memberAcademies/{academyId}")
    public ResponseEntity<ResponseDto> getByAcademy(@PathVariable(required = true) Long academyId,
                                           @ParameterObject @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<CommonMemberDto> memberAcademyDtoPage = memberAcademyService.getByAcademy(pageable, academyId);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.READ.getMessage())
                .data(memberAcademyDtoPage)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // update
//    @Operation(summary = "수정")
//    @PutMapping(value = "/admin/memberAcademy/{memberAcademyId}")
//    public ResponseEntity<ResponseDto> put(@PathVariable(required = true) Long memberAcademyId,
//                                           @RequestBody @Validated UpdateMemberAcademyDto updateMemberAcademyDto) throws Exception {
//
//        memberAcademyService.put(memberAcademyId, updateMemberAcademyDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.UPDATE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }

    // delete
//    @Operation(summary = "삭제")
//    @DeleteMapping(value = "/admin/memberAcademy/{memberAcademyId}")
//    public ResponseEntity<ResponseDto> delete(@PathVariable(required = true) Long memberAcademyId) throws Exception {
//
//        memberAcademyService.delete(memberAcademyId);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.DELETE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
//
//    @Operation(summary = "일괄 삭제")
//    @DeleteMapping(value = "/admin/memberAcademys", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseDto> delete(@ModelAttribute IdListDto idListDto) throws Exception {
//
//        memberAcademyService.deleteAll(idListDto);
//
//        ResponseDto responseDto = ResponseDto.builder()
//                .message(ResponseCode.DELETE.getMessage())
//                .data("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
}
