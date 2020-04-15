package com.xabe.mqtt.producer.infrastructure.messaging;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.typesafe.config.Config;
import com.xabe.mqtt.producer.domain.entity.SensorDO;
import com.xabe.mqtt.producer.domain.repository.ProducerRepository;
import java.nio.charset.StandardCharsets;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Singleton
public class ProducerRepositoryImpl implements ProducerRepository {

  public static final String MQTT_TEMPERATURE_QOS = "mqtt.temperature.qos";

  public static final String MQTT_TEMPERATURE_TOPIC = "mqtt.temperature.topic";

  public static final String MQTT_HUMIDITY_QOS = "mqtt.humidity.qos";

  public static final String MQTT_HUMIDITY_TOPIC = "mqtt.humidity.topic";

  private final Logger logger = LoggerFactory.getLogger(ProducerRepositoryImpl.class);

  private final IMqttClient mqttClient;

  private final Config config;

  private final ObjectMapper objectMapper;

  @Inject
  public ProducerRepositoryImpl(final IMqttClient mqttClient, final Config config) {
    this.config = config;
    this.mqttClient = mqttClient;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    this.objectMapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
    this.objectMapper.setSerializationInclusion(Include.NON_NULL);
  }

  @PreDestroy
  public void disconnect() throws MqttException {
    this.logger.info("Disconnected");
    this.mqttClient.disconnect();
  }

  @Override
  public Void sendTemperature(final SensorDO sensorDO) {
    try {
      final MqttMessage message = new MqttMessage(this.objectMapper.writeValueAsString(sensorDO).getBytes(StandardCharsets.UTF_8));
      message.setQos(this.config.getInt(MQTT_TEMPERATURE_QOS));
      this.mqttClient.publish(this.config.getString(MQTT_TEMPERATURE_TOPIC), message);
      this.logger.info("Send sensor temperature mqtt {}", sensorDO);
    } catch (final MqttException | JsonProcessingException e) {
      this.logger.error("Error send message : ", e);
    }
    return null;
  }

  @Override
  public Void sendHumidity(final SensorDO sensorDO) {
    try {
      final MqttMessage message = new MqttMessage(this.objectMapper.writeValueAsString(sensorDO).getBytes(StandardCharsets.UTF_8));
      message.setQos(this.config.getInt(MQTT_HUMIDITY_QOS));
      this.mqttClient.publish(this.config.getString(MQTT_HUMIDITY_TOPIC), message);
      this.logger.info("Send sensor humidity mqtt {}", sensorDO);
    } catch (final MqttException | JsonProcessingException e) {
      this.logger.error("Error send message : ", e);
    }
    return null;
  }
}
