package com.services.dm.endpoints;

import com.services.dm.dto.CandidateDTO;
import com.services.dm.dto.DocumentDTO;
import com.services.dm.dto.StatusDTO;
import com.services.dm.services.DocumentService;
import com.services.dm.util.DocumentUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

class DocumentControllerTest {

    DocumentService documentService;
    DocumentUtil documentUtil;
    DocumentController documentController;

    DocumentControllerTest() {
        documentService = Mockito.mock(DocumentService.class);
        documentUtil = Mockito.mock(DocumentUtil.class);
        documentController = new DocumentController(documentService,documentUtil);
    }


    @Test
    void addDocument() {
        Assert.assertNotNull(documentController.addDocument(getDocumentDTO()));
    }

    @Test
    void getDocuments() {
        Map<String, JSONArray> map = new HashMap<>();
        when(documentService.getDocuments()).thenReturn(List.of(getDocumentDTO()));
        when(documentUtil.convertDocumentDTOtoJSONObject(List.of(getDocumentDTO())))
                .thenReturn(new JSONObject(map));
        Assert.assertNotNull(documentController.getDocuments());
    }

    @Test
    void submitRequiredDocumentList() {
        Assert.assertNotNull(documentController.submitRequiredDocumentList(getStatusDTO()));
    }

    @Test
    void getRequiredDocumentListForUser() {
        when(documentService.getRequiredDocumentListForUser("test"))
                .thenReturn(new JSONObject());
        Assert.assertNotNull(documentController.getRequiredDocumentListForUser("test"));
    }

    @Test
    void updateCandidatesDocumentStatus() {
        Assert.assertNotNull(documentController.updateCandidatesDocumentStatus("test","test"));
    }

    @Test
    void getCandidatesUnderRecruiter() {
        when(documentService.getCandidatesUnderRecruiter("test"))
                .thenReturn(List.of(getCandidateDTO()));
    }

    private CandidateDTO getCandidateDTO() {
        return CandidateDTO.builder()
        .candidateEmail("test@mail.com").candidateFirstName("fname")
        .candidateLastName("lname").candidatePhoneNumber("123345").build();
    }

    private StatusDTO getStatusDTO() {
        return StatusDTO.builder()
                .candidateId("test")
                .recruiterId("test").build();
    }

    private DocumentDTO getDocumentDTO() {
        return DocumentDTO.builder()
                .id("test")
                .name("test")
                .type("test").build();
    }
}