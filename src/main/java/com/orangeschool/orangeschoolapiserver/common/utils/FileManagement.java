package com.orangeschool.orangeschoolapiserver.common.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
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

    // env
    @Value("${spring.profiles.active}")
    private String env;

    // local
    @Value("${folderPath}")
    private String folderPath;
    @Value("${resourcePath}")
    private String resourcePath;
    @Value("${serverUri}")
    private String serverUri;

    // s3
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private String folder = "/files";

    private static final Logger logger = LoggerFactory.getLogger(FileManagement.class);

    public String save(MultipartFile file, String serverFileName) throws Exception {
        if (env.compareTo("dev") == 0) {
            File makeFolder = new File(folderPath);
            if (!makeFolder.exists()) {
                makeFolder.mkdir();
            }
            File saveFile = new File(folderPath, serverFileName);
            file.transferTo(saveFile);
            String fileUrl = serverUri + resourcePath + "/" + serverFileName;

            return fileUrl;
        } else {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            amazonS3.putObject(
                    new PutObjectRequest(bucket + folder, serverFileName, file.getInputStream(), objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

//            String fileUrl = amazonS3.getUrl(bucket + folder, serverFileName).toString();
            String fileUrl = "https://media.orangeschool.kr" + folder + "/" + serverFileName;

            return fileUrl;
        }
    }

    public void delete(String serverFileName) throws Exception {
        if (env.compareTo("dev") == 0) {
            String fileUrl = folderPath + "/" + serverFileName;
            File file = new File(fileUrl);
            file.delete();
        } else {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket + folder, serverFileName));
        }
    }

    public byte[] getFile(String serverFileName) throws Exception {
        if (env.compareTo("dev") == 0) {
            String fileUrl = folderPath + "/" + serverFileName;
            File file = new File(fileUrl);
            return Files.readAllBytes(file.toPath());
        } else {
            S3Object o = amazonS3.getObject(new GetObjectRequest(bucket + folder, serverFileName));
            S3ObjectInputStream objectInputStream = o.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);
            return bytes;
        }
    }

    public String createServerFileName(MultipartFile file) {
        String extention = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        return UUID.randomUUID().toString() + "." + extention;
    }
}
