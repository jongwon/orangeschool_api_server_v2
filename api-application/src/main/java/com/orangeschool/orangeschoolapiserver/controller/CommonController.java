package com.orangeschool.orangeschoolapiserver.common;

import com.orangeschool.orangeschoolapiserver.common.dto.request.FileDownloadDto;
import com.orangeschool.orangeschoolapiserver.common.dto.response.FileUploadDto;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseDto;
import com.orangeschool.orangeschoolapiserver.common.utils.FileManagement;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.List;

@Tag(name = "유틸", description = "")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommonController {

    private final CommonService commonService;
    private final FileManagement fileManagement;

    @Operation(summary = "파일 업로드")
    @PostMapping(value = "/common/file")
    public ResponseEntity<ResponseDto> postFile(@RequestPart(required = true) MultipartFile file) throws Exception {

        FileUploadDto fileUploadDto = commonService.postFile(file);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(fileUploadDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "파일 삭제")
    @DeleteMapping(value = "/common/file")
    public ResponseEntity<ResponseDto> deleteFile(String serverFileName) throws Exception {

        commonService.deleteFile(serverFileName);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "파일 다운로드")
    @PostMapping("/common/file/download")
    public ResponseEntity getFile(@RequestBody FileDownloadDto downloadDto) throws Exception {
        byte[] bytes = fileManagement.getFile(downloadDto.getServerFileName());
        String fileName = URLEncoder.encode(downloadDto.getOriginalFileName(), "UTF-8");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        httpHeaders.add("Access-Control-Expose-Headers", "Content-Disposition");
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    private final CommonMemberRepository commonMemberRepository;

    @Operation(summary = "테스트")
    @PostMapping(value = "/test")
    public ResponseEntity<ResponseDto> test() throws Exception {

        List<String> tokenList = commonMemberRepository.findAdPushTokenByMemberType(MemberType.CHILD);

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.SUCCESS.getMessage())
                .data(tokenList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
