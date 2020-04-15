package com.xabe.mqtt.consumer.infrastructure.messaging.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xabe.mqtt.consumer.infrastructure.messaging.MqttHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandler<T> implements MqttHandler {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected final ObjectMapper objectMapper;

  private final String topic;

  private final Class<T> clazz;

  private final Consumer<T> consumer;

  protected AbstractHandler(final ObjectMapper objectMapper, final String topic, final Class<T> clazz, final Consumer<T> consumer) {
    this.objectMapper = objectMapper;
    this.topic = topic;
    this.clazz = clazz;
    this.consumer = consumer;
  }

  @Override
  public String getTopic() {
    return this.topic;
  }

  @Override
  public void handler(final MqttMessage mqttMessage) throws IOException {
    try {
      final String value = new String(mqttMessage.getPayload(), StandardCharsets.UTF_8);
      this.consumer.accept(this.objectMapper.readValue(value, this.clazz));
      this.logger.info("Read message {}", mqttMessage);
    } catch (final Exception e) {
      this.logger.error("Error read message : ", e);
    }
  }
}
