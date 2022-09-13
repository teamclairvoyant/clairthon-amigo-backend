package com.services.dm.endpoints;

import com.services.dm.dto.FileDownloadDTO;
import com.services.dm.dto.FileUploadRequestDTO;
import com.services.dm.dto.ResourceResponseDTO;
import com.services.dm.services.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Slf4j
@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @PostMapping(
            path = "/upload",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceResponseDTO> uploadFile(
            @RequestPart(required = true) String type,
            @RequestPart(required = false) String description,
            @RequestPart(required = true) MultipartFile file)
            throws Exception {
        try {
            FileUploadRequestDTO uploadDTO = FileUploadRequestDTO
                        .builder()
                        .type(type)
                        .description(description)
                        .inputStream(file.getInputStream())
                        .fileName(file.getOriginalFilename())
                        .fileSize(file.getSize())
                        .build();

            ResourceResponseDTO resource = documentService.uploadFile(uploadDTO);
            return ResponseEntity.ok().body(resource);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String id,
                                                          @RequestParam String fileName) throws Exception {
        try {

            ResponseEntity responseEntity;
            FileDownloadDTO fileDownloadDTO = documentService.downloadFile(id,fileName);
                responseEntity = ResponseEntity.ok()
                        .contentLength(fileDownloadDTO.getResource().contentLength())
                        .contentType(
                                MediaType.parseMediaType(
                                        fileDownloadDTO.getMimeType() != null
                                                ? fileDownloadDTO.getMimeType()
                                                : MediaType.APPLICATION_OCTET_STREAM.toString()))
                        .header(
                                HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + fileDownloadDTO.getName() + "\"")
                        .body(fileDownloadDTO.getResource());


            return responseEntity;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}
