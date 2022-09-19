package com.services.dm.repositories;

import com.services.dm.constants.Constant;
import com.services.dm.constants.DBConstants;
import com.services.dm.dto.DocumentDTO;
import com.services.dm.properties.DBProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ConditionCheck;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

import javax.annotation.PostConstruct;

@Repository
@Slf4j
public class DocumentRepository {

    private DynamoDbTable<DocumentDTO> table;

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
}
