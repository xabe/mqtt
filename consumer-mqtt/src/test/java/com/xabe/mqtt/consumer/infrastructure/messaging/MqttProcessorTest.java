package com.xabe.mqtt.consumer.infrastructure.messaging;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.typesafe.config.Config;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MqttProcessorTest {

  private IMqttClient mqttClient;

  private Config config;

  private MqttHandler mqttHandler;

  private MqttCallback mqttCallback;

  @BeforeEach
  public void setUp() throws Exception {
    this.mqttClient = mock(IMqttClient.class);
    this.config = mock(Config.class);
    this.mqttHandler = mock(MqttHandler.class);
    when(this.mqttHandler.getTopic()).thenReturn("test");
    when(this.config.getString(MqttProcessor.MQTT_HUMIDITY_TOPIC)).thenReturn("humidity");
    when(this.config.getString(MqttProcessor.MQTT_TEMPERATURE_TOPIC)).thenReturn("temperature");
    this.mqttCallback = new MqttProcessor(this.mqttClient, this.config, Collections.singletonList(this.mqttHandler));
  }

  @Test
  public void verifySetUp() throws Exception {
    verify(this.mqttClient).subscribe("humidity");
    verify(this.mqttClient).subscribe("temperature");
    verify(this.mqttClient).setCallback(this.mqttCallback);
  }

  @Test
  public void shouldHandleMessage() throws Exception {
    final String topic = "test";
    final MqttMessage mqttMessage = new MqttMessage("{}".getBytes(StandardCharsets.UTF_8));

    this.mqttCallback.messageArrived(topic, mqttMessage);

    verify(this.mqttHandler).handler(eq(mqttMessage));
  }
}