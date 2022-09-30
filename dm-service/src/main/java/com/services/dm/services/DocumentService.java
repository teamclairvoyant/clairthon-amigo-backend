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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
            if(os.getKey().contains(userId.concat("/" + description + "/"))) {
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

    public FileDownloadDTO downloadAll(String userId)
            throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);

        try {
            ZipEntry zipentry = null;
            Set<String> objectKeys= new HashSet<>();
            ListObjectsV2Result result = s3Client.listObjectsV2(Constant.S3_BUCKET_NAME);
            List<S3ObjectSummary> objects = result.getObjectSummaries();
            for (S3ObjectSummary os : objects) {
                if(os.getKey().contains(userId)) {
                    objectKeys.add(os.getKey());
                }
            }
            for (String objectKey : objectKeys) {
                String name = objectKey.substring(objectKey.lastIndexOf("/"), objectKey.length());
                zipentry = new ZipEntry(objectKey);
                zos.putNextEntry(zipentry);
                InputStream in = downloadFileAsStream(Constant.S3_BUCKET_NAME, objectKey,s3Client);
                zos.write(in.readAllBytes());
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zos.flush();
            zos.closeEntry();
            zos.close();
            bos.close();
        }
        return FileDownloadDTO.builder()
                .mimeType(Constant.APPLICATION_ZIP)
                .name(Constant.ZIP_FILE)
                .resource(new ByteArrayResource(bos.toByteArray()))
                .build();
    }

    public static InputStream downloadFileAsStream(String bucketName, String objectKey,AmazonS3 s3Client) {
        try {
            GetObjectRequest s3ObjectReq = new GetObjectRequest(bucketName, objectKey);
            return s3Client.getObject(s3ObjectReq).getObjectContent();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
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

    public List<CandidateDTO> getCandidatesUnderRecruiter(String recruiterId) {
        return documentRepository.getCandidatesUnderRecruiter(recruiterId);
    }
}
