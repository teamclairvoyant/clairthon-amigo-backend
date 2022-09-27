package com.services.dm.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = DBProperties.PREFIX)
@Getter
@Setter
@ToString
@Configuration
public class DBProperties {

  protected static final String PREFIX = "db.dynamo";
  private String region;

}