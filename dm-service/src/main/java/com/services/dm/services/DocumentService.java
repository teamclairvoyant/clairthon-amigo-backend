package com.services.dm.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.services.dm.constants.Constant;
import com.services.dm.dto.*;
import com.services.dm.repositories.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class DocumentService {

    private final AmazonS3 s3Client;

    private final DocumentRepository documentRepository;

    public DocumentService(final AmazonS3 s3Client, final DocumentRepository documentRepository) {
        this.s3Client = s3Client;
        this.documentRepository = documentRepository;
    }


    public ResourceResponseDTO uploadFile(FileUploadRequestDTO uploadDTO) throws Exception {

        try {
            String fileKey = uploadDTO.getUserId()
                    + Constant.PATH_SEPARATOR
                    + uploadDTO.getDescription()
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

            documentRepository.updateStatusOfSubmittedDocuments(uploadDTO.getUserId(), uploadDTO.getDescription());

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

    public FileDownloadDTO downloadFile(String userId,String description) throws IOException {
        String fileKey= "";
        ListObjectsV2Result result = s3Client.listObjectsV2(Constant.S3_BUCKET_NAME);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            if(os.getKey().contains(description)) {
                fileKey = os.getKey();
                break;
            }
        }
        String fileKeyArr[] = fileKey.split("/");
        String fileName = fileKeyArr[fileKeyArr.length-1];
        S3Object  s3Object = s3Client.getObject(Constant.S3_BUCKET_NAME,fileKey);
        return FileDownloadDTO.builder()
                .mimeType(s3Object.getObjectMetadata().getContentType())
                .name(fileName)
                .resource(new ByteArrayResource(s3Object.getObjectContent().readAllBytes()))
                .build();
    }

    public void addDocument(DocumentDTO documentDTO) {
        documentRepository.addDocument(documentDTO);
    }

    public List<DocumentDTO> getDocuments() {
        return documentRepository.getDocuments();
    }

    public void submitRequiredDocumentList(StatusDTO statusDTO) {
        log.info("Document List {} for userId {} ", statusDTO.getDocuments(), statusDTO.getCandidateId());
        documentRepository.submitRequiredDocumentList(statusDTO);
    }

    public JSONObject getRequiredDocumentListForUser(String candidateId) {
        return documentRepository.getRequiredDocumentListForUser(candidateId).getDocuments();
    }

    public void updateCandidatesDocumentStatus(String candidateId, String candidateStatus) {
        documentRepository.updateCandidatesDocumentStatus(candidateId, candidateStatus);
    }
}
