package com.services.dm.endpoints;

import com.services.dm.constants.Constant;
import com.services.dm.dto.DocumentDTO;
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

@Slf4j
@RestController
@RequestMapping(DocumentController.BASE_URI)
public class DocumentController {

    static final String BASE_URI = "/api/";

    private final DocumentService documentService;

    public DocumentController(final DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(
            path = Constant.UPLOAD_URI,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceResponseDTO> uploadFile(
            @RequestPart String type,
            @RequestPart(required = false) String description,
            @RequestPart MultipartFile file)
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

    @GetMapping(Constant.DOWNLOAD_FILE_URI)
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String id,
                                                          @RequestParam String fileName) throws Exception {
        try {

            ResponseEntity responseEntity;
            FileDownloadDTO fileDownloadDTO = documentService.downloadFile(id, fileName);
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

    @PostMapping(Constant.DOCUMENT_URI)
    public DocumentDTO addDocument(@RequestBody DocumentDTO documentDTO) {
        documentService.addDocument(documentDTO);
        return documentDTO;
    }
}
