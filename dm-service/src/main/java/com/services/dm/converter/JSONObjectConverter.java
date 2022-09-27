package com.services.dm.converter;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Slf4j
public class JSONObjectConverter implements AttributeConverter<JSONObject> {
  @Override
  public AttributeValue transformFrom(JSONObject jsonObject) {
    return AttributeValue.builder().s(jsonObject.toString()).build();
  }

  /**
   * Transform string into JSONObject.
   * 
   * @param attributeValue
   *          The AttributeValue
   * @return JSONObject
   */
  @Override
  public JSONObject transformTo(AttributeValue attributeValue) {
    log.debug("[JSONObjectConverter] :: START transformTo.");
    try {
      log.debug("Converting String into JSONObject {}", attributeValue.s());
      return (JSONObject) JSONValue.parse(attributeValue.s());
    } catch (Exception e) {
      log.error("Error while converting string into JSONObject. String is {}", attributeValue.s());
      throw new RuntimeException(e);
    }
  }

  @Override
  public EnhancedType<JSONObject> type() {
    return EnhancedType.of(JSONObject.class);
  }

  @Override
  public AttributeValueType attributeValueType() {
    return AttributeValueType.S;
  }
}
