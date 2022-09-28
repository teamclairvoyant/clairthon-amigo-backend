package com.services.dm.repositories;

import com.services.dm.constants.Constant;
import com.services.dm.constants.DBConstants;
import com.services.dm.dto.DocumentDTO;
import com.services.dm.dto.StatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ConditionCheck;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class DocumentRepository {

    private DynamoDbTable<DocumentDTO> table;

    private DynamoDbTable<StatusDTO> statusTable;

    private final DynamoDbEnhancedClient client;

    public DocumentRepository(DynamoDbEnhancedClient client) {
        this.client = client;
    }

    @PostConstruct
    public void initializeTableClient() {
        log.debug("Loading dynamo DB table name: {}",
                Constant.DOCUMENT_TABLE);
        table = client.table(Constant.DOCUMENT_TABLE,
                TableSchema.fromBean(DocumentDTO.class));
        statusTable = client.table(Constant.STATUS_TABLE,
                TableSchema.fromBean(StatusDTO.class));
    }

    public void addDocument(DocumentDTO documentDTO) {

        log.debug("In addDocument method::");

        try {
            client.transactWriteItems(enhancedRequest -> {
                Expression expression = Expression.builder()
                        .expression("attribute_not_exists(" + DBConstants.COL_ID + ")").build();
                Key key = Key.builder().partitionValue(documentDTO.getId())
                        .build();
                enhancedRequest.addConditionCheck(table,
                        ConditionCheck.builder().key(key).conditionExpression(expression).build());
            });

            client.transactWriteItems(
                    enhancedRequest -> enhancedRequest.addPutItem(table, documentDTO));

            log.debug(
                    "Document Added Successfully::");

        } catch (TransactionCanceledException e) {
            log.error(
                    "Adding document failed with error {}",
                    e.getMessage());
        } catch (Exception e) {
            log.error(
                    "Adding document failed with error: {}",
                    e.getMessage());
        }
    }

    public List<DocumentDTO> getDocuments() {

        log.debug("In getDocuments method::");

        try {
            return table.scan().items().stream()
                    .collect(Collectors.toList());
        } catch (TransactionCanceledException e) {
            log.error(
                    "Getting document failed with error {}",
                    e.getMessage());
        } catch (Exception e) {
            log.error(
                    "Getting document failed with error: {}",
                    e.getMessage());
        }
        return Collections.emptyList();
    }

    public void submitRequiredDocumentList(StatusDTO statusDTO) {

        log.debug("In submitRequiredDocumentList method::");

        try {
            client.transactWriteItems(enhancedRequest -> {
                Expression expression = Expression.builder()
                        .expression("attribute_not_exists(" + DBConstants.COL_ID + ")").build();
                Key key = Key.builder().partitionValue(statusDTO.getCandidateId())
                        .build();
                enhancedRequest.addConditionCheck(table,
                        ConditionCheck.builder().key(key).conditionExpression(expression).build());
            });

            client.transactWriteItems(
                    enhancedRequest -> enhancedRequest.addPutItem(statusTable, statusDTO));

            log.debug(
                    "Submitting required list of document Added Successfully::");

        } catch (TransactionCanceledException e) {
            log.error(
                    "Submitting required list of document failed with error {}",
                    e.getMessage());
        } catch (Exception e) {
            log.error(
                    "Submitting required list of document failed with error: {}",
                    e.getMessage());
        }
    }

    public void updateStatusOfSubmittedDocuments(
            String candidateId, String documentId) {
        Key key = Key.builder().partitionValue(candidateId)
                .build();


        QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key)).build();
        List<StatusDTO> statusDTOS = statusTable.query(queryEnhancedRequest).items().stream().collect(Collectors.toList());

        if (statusDTOS.size() == 1) {

            StatusDTO statusDTO = statusDTOS.get(0);
            JSONObject documents = statusDTO.getDocuments();
            documents.put(documentId, "true");
            statusDTO.setDocuments(documents);

            client.transactWriteItems(
                    enhancedRequest -> enhancedRequest.addUpdateItem(statusTable, statusDTO));
        }

    }

    public StatusDTO getRequiredDocumentListForUser(String candidateId) {

        log.debug("In getRequiredDocumentListForUser method::");

        try {
            Key key = Key.builder().partitionValue(candidateId)
                    .build();


            QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest.builder()
                    .queryConditional(QueryConditional.keyEqualTo(key)).build();
            List<StatusDTO> statusDTOS = statusTable.query(queryEnhancedRequest).items().stream().collect(Collectors.toList());
            if (statusDTOS.size() == 1) {
                return statusDTOS.get(0);
            }
        } catch (TransactionCanceledException e) {
            log.error(
                    "Getting required document failed with error {}",
                    e.getMessage());
        } catch (Exception e) {
            log.error(
                    "Getting required document failed with error: {}",
                    e.getMessage());
        }
        return new StatusDTO();
    }
}
