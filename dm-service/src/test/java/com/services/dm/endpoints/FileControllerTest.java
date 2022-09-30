package com.services.dm.endpoints;

import com.services.dm.dto.FileDownloadDTO;
import com.services.dm.dto.FileUploadRequestDTO;
import com.services.dm.dto.ResourceResponseDTO;
import com.services.dm.services.DocumentService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;;
import static org.mockito.Mockito.when;

class FileControllerTest {

    DocumentService documentService;
    FileController fileController;

    FileControllerTest() {
        documentService = Mockito.mock(DocumentService.class);
        fileController = new FileController(documentService);
    }


    @Test
    void uploadFile() throws Exception {
        MultipartFile file = new MockMultipartFile("name", "content".getBytes());
        when(documentService.uploadFile(getFileUploadDTO()))
                .thenReturn(getResourceResponseDTO());
        Assert.assertNotNull(fileController.uploadFile("test", "userId",file));
    }

    @Test
    void downloadFile() throws Exception {
        when(documentService.downloadFile("test","test")).thenReturn(getFileDownloadDTO());
        Assert.assertNotNull(fileController.downloadFile("test","test"));
    }

    @Test
    void downloadAll() throws Exception {
        when(documentService.downloadAll("test")).thenReturn(getFileDownloadDTO());
        Assert.assertNotNull(fileController.downloadAll("test"));
    }

    private FileDownloadDTO getFileDownloadDTO() {
        return FileDownloadDTO.builder()
                .resource(new ByteArrayResource("content".getBytes()))
                .mimeType("application/pdf").name("test").build();
    }

    private FileUploadRequestDTO getFileUploadDTO() {
        return FileUploadRequestDTO.builder()
        .fileName("fileName").bytes("content".getBytes()).build();
    }

    private ResourceResponseDTO getResourceResponseDTO() {
        return ResourceResponseDTO.builder()
        .description("test").fileName("fileName").build();
    }
 }