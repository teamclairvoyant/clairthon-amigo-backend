/*
 * PEARSON PROPRIETARY AND CONFIDENTIAL INFORMATION SUBJECT TO NDA 
 * Copyright (c) 2022 Pearson Education, Inc.
 * All Rights Reserved. 
 * 
 * NOTICE: All information contained herein is, and remains the property of 
 * Pearson Education, Inc. The intellectual and technical concepts contained 
 * herein are proprietary to Pearson Education, Inc. and may be covered by U.S. 
 * and Foreign Patents, patent applications, and are protected by trade secret 
 * or copyright law. Dissemination of this information, reproduction of this  
 * material, and copying or distribution of this software is strictly forbidden   
 * unless prior written permission is obtained from Pearson Education, Inc.
 */
package com.services.dm.config;

import com.services.dm.properties.DBProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Initializes database with the table for Course service.
 */
@Slf4j
@Configuration
public class DBConfig {

  private final DBProperties dbProperties;

  public DBConfig(final DBProperties dbProperties) {
    this.dbProperties = dbProperties;
  }

  @Bean
  public DynamoDbEnhancedClient getDefaultDynamoDBClient() {
    log.debug("[DBConfig] :: Connecting to AWS dynamodb instance in region: {} ",
        dbProperties.getRegion());
    return DynamoDbEnhancedClient.builder().dynamoDbClient(DynamoDbClient.builder()
        .region(Region.of(dbProperties.getRegion()))
        .build()).build();
  }
}