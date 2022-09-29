package com.services.dm.endpoints;

import com.services.dm.constants.Constant;
import com.services.dm.dto.*;
import com.services.dm.services.DocumentService;
import com.services.dm.util.DocumentUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(Constant.GET_CANDIDATE)
    public List<CandidateDTO> getCandidatesUnderRecruiter(@PathVariable String recruiterId) {
        return documentService.getCandidatesUnderRecruiter(recruiterId);
    }
}
