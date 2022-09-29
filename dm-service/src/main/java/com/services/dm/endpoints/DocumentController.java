package com.services.dm.endpoints;

import com.services.dm.constants.Constant;
import com.services.dm.dto.*;
import com.services.dm.services.DocumentService;
import com.services.dm.util.DocumentUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping(DocumentController.BASE_URI)
public class DocumentController {

    static final String BASE_URI = "/api/";

    private final DocumentService documentService;

    private final DocumentUtil documentUtil;

    public DocumentController(final DocumentService documentService, final DocumentUtil documentUtil) {
        this.documentService = documentService;
        this.documentUtil = documentUtil;
    }

    @PostMapping(
            path = Constant.UPLOAD_URI,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceResponseDTO> uploadFile(
            @RequestPart String description,
            @RequestPart String userId,
            @RequestPart MultipartFile file)
            throws Exception {
        try {
            FileUploadRequestDTO uploadDTO = FileUploadRequestDTO
                    .builder()
                    .description(description)
                    .inputStream(file.getInputStream())
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .userId(userId)
                    .build();

            ResourceResponseDTO resource = documentService.uploadFile(uploadDTO);
            return ResponseEntity.ok().body(resource);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping(Constant.DOWNLOAD_FILE_URI)
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String userId,
                                                          @RequestParam String description) throws Exception {
        try {

            ResponseEntity responseEntity;
            FileDownloadDTO fileDownloadDTO = documentService.downloadFile(userId, description);
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

    @GetMapping(Constant.DOWNLOAD_ALL_FILES_URI)
    public ResponseEntity<ByteArrayResource> downloadAll(@PathVariable String userId) throws Exception {
        try {
            FileDownloadDTO fileDownloadDTO = documentService.downloadAll(userId);
            return ResponseEntity.ok()
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

    @GetMapping(Constant.DOCUMENT_URI)
    public JSONObject getDocuments() {
        return documentUtil.convertDocumentDTOtoJSONObject(documentService.getDocuments());
    }

    @PostMapping(Constant.SUBMIT)
    public String submitRequiredDocumentList(@RequestBody StatusDTO statusDTO) {
        documentService.submitRequiredDocumentList(statusDTO);
        return Constant.DOCUMENT_LIST_SUBMITTED_SUCCESSFULLY;
    }

    @GetMapping(Constant.REQUIRED_DOCUMENTS)
    public JSONObject getRequiredDocumentListForUser(@PathVariable String candidateId) {
        return documentService.getRequiredDocumentListForUser(candidateId);
    }

    @PutMapping(Constant.UPDATE_STATUS)
    public String updateCandidatesDocumentStatus(@PathVariable String candidateId, @PathVariable String candidateStatus) {
        documentService.updateCandidatesDocumentStatus(candidateId, candidateStatus);
        return Constant.STATUS_UPDATED_SUCCESSFULLY;
    }
}
