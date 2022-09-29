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
public class CandidateDTO {

    @Getter(onMethod_ = { @DynamoDbPartitionKey, @DynamoDbAttribute(value = "candidateId") })
    private String candidateId;

    private String candidateEmail;

    private String candidateFirstName;

    private String candidateLastName;

    private String candidatePhoneNumber;

    private String candidateStatus;

    private String recruiterId;
}
