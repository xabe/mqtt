package com.xabe.mqtt.consumer.infrastructure.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.ws.rs.ext.ContextResolver;

public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

  private final ObjectMapper mapper;

  public ObjectMapperContextResolver() {
    this.mapper = new ObjectMapper();
    this.mapper.registerModule(new JavaTimeModule());
    this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    this.mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    this.mapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return this.mapper;
  }

}
