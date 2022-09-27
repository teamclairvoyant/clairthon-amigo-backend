package com.services.dm.dto;

import com.services.dm.constants.DBConstants;
import com.services.dm.converter.JSONObjectConverter;
import lombok.*;
import org.json.simple.JSONObject;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@DynamoDbBean
public class StatusDTO {

    @Getter(onMethod_ = { @DynamoDbPartitionKey, @DynamoDbAttribute(value = "candidateId") })
    private String candidateId;

    private String recruiterId;

    @Getter(onMethod_ = { @DynamoDbConvertedBy(JSONObjectConverter.class) })
    private JSONObject documents;
}
