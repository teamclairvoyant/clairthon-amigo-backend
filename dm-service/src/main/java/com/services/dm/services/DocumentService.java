package com.services.dm.services;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.services.dm.constants.Constant;
import com.services.dm.dto.FileDownloadDTO;
import com.services.dm.dto.FileUploadRequestDTO;
import com.services.dm.dto.ResourceResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;

@Slf4j
@Service
public class DocumentService {

    AmazonS3 s3Client;

    public DocumentService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }


    public ResourceResponseDTO uploadFile(FileUploadRequestDTO uploadDTO) throws Exception {

        try {
            String fileKey = UUID.randomUUID().toString()
                    + Constant.PATH_SEPARATOR
                    + uploadDTO.getFileName();

            byte[] bytes = IOUtils.toByteArray(uploadDTO.getInputStream());

            long size = bytes.length;
            ObjectMetadata s3ObjMetadata = new ObjectMetadata();
            s3ObjMetadata.setContentLength(size);

            // bucket name will be set by S3ClientService
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(Constant.S3_BUCKET_NAME, fileKey, byteArrayInputStream, s3ObjMetadata);
            s3Client.putObject(putObjectRequest);

            return ResourceResponseDTO.builder()
                    .description(uploadDTO.getDescription())
                    .fileName(uploadDTO.getFileName())
                    .location(fileKey)
                    .fileSize(uploadDTO.getFileSize())
                    .resourceType(uploadDTO.getType())
                    .fileType(FilenameUtils.getExtension(uploadDTO.getFileName()).toLowerCase())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public FileDownloadDTO downloadFile(String id,String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(Constant.S3_BUCKET_NAME,id.concat("/"+fileName));
        return FileDownloadDTO.builder()
                .mimeType(s3Object.getObjectMetadata().getContentType())
                .name(fileName)
                .resource(new ByteArrayResource(s3Object.getObjectContent().readAllBytes()))
                .build();
    }
}
