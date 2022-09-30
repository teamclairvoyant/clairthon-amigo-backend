package com.services.dm.services;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.services.dm.constants.Constant;
import com.services.dm.dto.*;
import com.services.dm.repositories.DocumentRepository;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMultipartHttpServletRequestDsl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DocumentServiceTest {


    AmazonS3 s3Client;
    DocumentRepository documentRepository;
    DocumentService documentService;

    public DocumentServiceTest() {
        s3Client = Mockito.mock(AmazonS3.class);
        documentRepository = Mockito.mock(DocumentRepository.class);
        documentService = new DocumentService(s3Client,documentRepository);
    }

    @Test
    void uploadFile() throws Exception {
        Assert.assertNotNull(documentService.uploadFile(getFileUploadDTO()));
    }

    @Test
    void downloadFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("test","contant".getBytes());
        when(s3Client.listObjectsV2(Constant.S3_BUCKET_NAME))
                .thenReturn(new ListObjectsV2Result());
        S3Object s3Object = new S3Object();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setUserMetadata(Map.of());
        s3Object.setObjectMetadata(objectMetadata);
        s3Object.setObjectContent(file.getInputStream());
        when(s3Client.getObject(Constant.S3_BUCKET_NAME,""))
                .thenReturn(s3Object);
        Assert.assertNotNull(documentService.downloadFile("userId","description"));
    }

    @Test
    void downloadAll() throws IOException {
        MockMultipartFile file = new MockMultipartFile("test","contant".getBytes());
        when(s3Client.listObjectsV2(Constant.S3_BUCKET_NAME))
                .thenReturn(new ListObjectsV2Result());
        S3Object s3Object = new S3Object();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setUserMetadata(Map.of());
        s3Object.setObjectMetadata(objectMetadata);
        s3Object.setObjectContent(file.getInputStream());
        when(s3Client.getObject(Constant.S3_BUCKET_NAME,""))
                .thenReturn(s3Object);
        Assert.assertNotNull(documentService.downloadAll("userid"));
    }

    @Test
    void downloadFileAsStream() throws IOException {
        MockMultipartFile file = new MockMultipartFile("test","contant".getBytes());
        S3Object s3Object = new S3Object();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setUserMetadata(Map.of());
        s3Object.setObjectMetadata(objectMetadata);
        s3Object.setObjectContent(file.getInputStream());
        when(s3Client.getObject(new GetObjectRequest("test","test")))
                .thenReturn(s3Object);
        Assert.assertNotNull(DocumentService.downloadFileAsStream("test","test",s3Client));
    }

    @Test
    void addDocument() {
        documentService.addDocument(getDocumentDTO());
    }

    @Test
    void getDocuments() {
        when(documentRepository.getDocuments())
                .thenReturn(List.of(getDocumentDTO()));
        Assert.assertNotNull(documentService.getDocuments());
    }

    @Test
    void submitRequiredDocumentList() {
        documentService.submitRequiredDocumentList(getStatusDTO());
    }


    @Test
    void updateCandidatesDocumentStatus() {
        documentRepository.updateCandidatesDocumentStatus("test","test");
    }

    @Test
    void getCandidatesUnderRecruiter() {
        documentRepository.getCandidatesUnderRecruiter("test");
    }

    private DocumentDTO getDocumentDTO() {
        return DocumentDTO.builder()
                .id("test")
                .name("test")
                .type("test").build();
    }


    private StatusDTO getStatusDTO() {
        return StatusDTO.builder().candidateId("test").build();
    }

    private FileDownloadDTO getFileDownloadDTO() {
        return FileDownloadDTO.builder()
                .resource(new ByteArrayResource("content".getBytes()))
                .mimeType("application/pdf").name("test").build();
    }

    private FileUploadRequestDTO getFileUploadDTO() throws IOException {
        MockMultipartFile file = new MockMultipartFile("test","contant".getBytes());
        return FileUploadRequestDTO.builder()
                .fileName("fileName")
                .inputStream(file.getInputStream())
                .bytes("content".getBytes()).build();
    }

    private ResourceResponseDTO getResourceResponseDTO() {
        return ResourceResponseDTO.builder()
                .description("test").fileName("fileName").build();
    }

}

