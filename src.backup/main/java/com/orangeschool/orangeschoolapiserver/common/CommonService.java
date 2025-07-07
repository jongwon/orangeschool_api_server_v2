package com.orangeschool.orangeschoolapiserver.common;

import com.orangeschool.orangeschoolapiserver.common.dto.response.FileUploadDto;
import com.orangeschool.orangeschoolapiserver.common.utils.FileManagement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final FileManagement fileManagement;

    @Transactional
    public FileUploadDto postFile(MultipartFile file) throws Exception {
        String serverFileName = fileManagement.createServerFileName(file);
        String fileUrl = fileManagement.save(file, serverFileName);
        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setServerFileName(serverFileName);
        fileUploadDto.setFileUrl(fileUrl);
        return fileUploadDto;
    }

    public void deleteFile(String serverFileName) throws Exception {
        fileManagement.delete(serverFileName);
    }



}
