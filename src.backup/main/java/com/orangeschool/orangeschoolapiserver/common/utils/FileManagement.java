package com.orangeschool.orangeschoolapiserver.common.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileManagement {

    @Value("${folderPath}")
    private String folderPath;
    @Value("${resourcePath}")
    private String resourcePath;
    @Value("${serverUri}")
    private String serverUri;

    private static final Logger logger = LoggerFactory.getLogger(FileManagement.class);

    public String save(MultipartFile file, String serverFileName) throws Exception {
        File makeFolder = new File(folderPath);
        if (!makeFolder.exists()) {
            makeFolder.mkdirs();
        }
        File saveFile = new File(folderPath, serverFileName);
        file.transferTo(saveFile);
        String fileUrl = serverUri + resourcePath + "/" + serverFileName;
        
        logger.info("File saved: {}", fileUrl);
        return fileUrl;
    }

    public void delete(String serverFileName) throws Exception {
        String filePath = folderPath + "/" + serverFileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            logger.info("File deleted: {}", filePath);
        } else {
            logger.warn("File not found for deletion: {}", filePath);
        }
    }

    public byte[] getFile(String serverFileName) throws Exception {
        String filePath = folderPath + "/" + serverFileName;
        File file = new File(filePath);
        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        } else {
            logger.error("File not found: {}", filePath);
            throw new Exception("File not found: " + serverFileName);
        }
    }

    public String createServerFileName(MultipartFile file) {
        String extention = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        return UUID.randomUUID().toString() + "." + extention;
    }
}
