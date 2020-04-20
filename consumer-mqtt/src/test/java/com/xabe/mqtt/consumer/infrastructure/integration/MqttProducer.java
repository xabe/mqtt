package com.xabe.mqtt.consumer.infrastructure.integration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.typesafe.config.Config;
import com.xabe.mqtt.consumer.infrastructure.presentation.payload.SensorPayload;
import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttProducer {

  private final Logger logger = LoggerFactory.getLogger(MqttProducer.class);

  private final ObjectMapper objectMapper;

  private final Config config;

  private final IMqttClient mqttClient;

  public MqttProducer(final Config config) {
    this.config = config;
    try {
      this.mqttClient = new MqttClient(config.getString("mqtt.temperature.broker"), "test-producer", new MemoryPersistence());
      final MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      connOpts.setKeepAliveInterval(30);
      connOpts.setUserName(config.getString("mqtt.temperature.user"));
      connOpts.setPassword(config.getString("mqtt.temperature.pass").toCharArray());
      this.mqttClient.connect(connOpts);
      this.objectMapper = new ObjectMapper();
      this.objectMapper.registerModule(new JavaTimeModule());
      this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
      this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      this.objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
      this.objectMapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
      this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
      this.objectMapper.setSerializationInclusion(Include.NON_NULL);
      this.logger.info("Create MqttProducer");
    } catch (final MqttException e) {
      this.logger.error("Error create MqttProducer ", e);
      throw new RuntimeException(e);
    }
  }

  public void sendSensor(final SensorPayload sensorPayload, final String topic) throws JsonProcessingException, MqttException {
    final MqttMessage message = new MqttMessage(this.objectMapper.writeValueAsString(sensorPayload).getBytes(StandardCharsets.UTF_8));
    message.setQos(2);
    this.mqttClient.publish(this.config.getString(topic), message);
  }
}
