package com.services.dm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.services.dm.constants.DBConstants;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDTO {

    @Getter(onMethod_ = { @DynamoDbPartitionKey, @DynamoDbAttribute(value = DBConstants.COL_ID) })
    private String id;

    private String type;

    private String name;
}
