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