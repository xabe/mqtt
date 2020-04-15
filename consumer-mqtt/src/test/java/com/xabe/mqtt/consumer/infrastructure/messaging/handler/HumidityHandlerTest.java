package com.xabe.mqtt.consumer.infrastructure.messaging.handler;

import static com.xabe.mqtt.consumer.infrastructure.messaging.MqttProcessor.MQTT_HUMIDITY_TOPIC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.typesafe.config.Config;
import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import com.xabe.mqtt.consumer.domain.repository.ConsumerRepository;
import com.xabe.mqtt.consumer.infrastructure.messaging.MqttHandler;
import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HumidityHandlerTest {

  private Config config;

  private ObjectMapper objectMapper;

  private ConsumerRepository consumerRepository;

  private MqttHandler mqttHandler;

  @BeforeEach
  public void setUp() throws Exception {
    this.config = mock(Config.class);
    when(this.config.getString(MQTT_HUMIDITY_TOPIC)).thenReturn("humidity");
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    this.objectMapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
    this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    this.consumerRepository = mock(ConsumerRepository.class);
    this.mqttHandler = new HumidityHandler(this.consumerRepository, this.objectMapper, this.config);
  }

  @Test
  public void getTopic() throws Exception {
    assertThat(this.mqttHandler.getTopic(), is(notNullValue()));
    assertThat(this.mqttHandler.getTopic(), is("humidity"));
  }

  @Test
  public void shouldHandlerMessage() throws Exception {
    final MqttMessage mqttMessage = new MqttMessage();
    final SensorDO sensorDO = SensorDO.builder().unit("PERCENTAGE").value("10").build();
    final byte[] bytes = this.objectMapper.writeValueAsString(sensorDO).getBytes(StandardCharsets.UTF_8);
    mqttMessage.setPayload(bytes);

    this.mqttHandler.handler(mqttMessage);

    verify(this.consumerRepository).addHumidity(eq(sensorDO));
  }

  @Test
  public void NotShouldHandlerMessageError() throws Exception {
    final MqttMessage mqttMessage = new MqttMessage();
    mqttMessage.setPayload("error".getBytes(StandardCharsets.UTF_8));

    this.mqttHandler.handler(mqttMessage);

    verify(this.consumerRepository, never()).addTemperature(any());

  }

}