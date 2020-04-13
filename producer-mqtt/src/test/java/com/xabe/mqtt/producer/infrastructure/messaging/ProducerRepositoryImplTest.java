package com.xabe.mqtt.producer.infrastructure.messaging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.typesafe.config.Config;
import com.xabe.mqtt.producer.domain.entity.SensorDO;
import com.xabe.mqtt.producer.domain.repository.ProducerRepository;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ProducerRepositoryImplTest {

  private Config config;

  private IMqttClient mqttClient;

  private Gson gson;

  private ProducerRepository producerRepository;

  @BeforeEach
  public void setUp() throws Exception {
    this.gson = new Gson();
    this.config = mock(Config.class);
    this.mqttClient = mock(IMqttClient.class);
    this.producerRepository = new ProducerRepositoryImpl(this.mqttClient, this.config);
  }

  @Test
  public void shouldDisconnect() throws Exception {

    ProducerRepositoryImpl.class.cast(this.producerRepository).disconnect();

    verify(this.mqttClient).disconnect();
  }

  @Test
  public void shouldSendMessageSensorTemperature() throws Exception {
    final SensorDO sensorDO = SensorDO.builder().unit("CELSIUS").value("10").build();
    final ArgumentCaptor<MqttMessage> argumentCaptor = ArgumentCaptor.forClass(MqttMessage.class);

    when(this.config.getInt(eq(ProducerRepositoryImpl.MQTT_TEMPERATURE_QOS))).thenReturn(2);
    when(this.config.getString(eq(ProducerRepositoryImpl.MQTT_TEMPERATURE_TOPIC))).thenReturn("test");

    this.producerRepository.sendTemperature(sensorDO);

    verify(this.mqttClient).publish(eq("test"), argumentCaptor.capture());
    final MqttMessage result = argumentCaptor.getValue();
    assertThat(result, is(notNullValue()));
    assertThat(result.getQos(), is(2));
    assertThat(result.getPayload(), is(this.gson.toJson(sensorDO).getBytes()));
  }

  @Test
  public void shouldSendMessageSensorHumidity() throws Exception {
    final SensorDO sensorDO = SensorDO.builder().unit("HUMIDITY").value("10").build();
    final ArgumentCaptor<MqttMessage> argumentCaptor = ArgumentCaptor.forClass(MqttMessage.class);

    when(this.config.getInt(eq(ProducerRepositoryImpl.MQTT_HUMIDITY_QOS))).thenReturn(1);
    when(this.config.getString(eq(ProducerRepositoryImpl.MQTT_HUMIDITY_TOPIC))).thenReturn("test");

    this.producerRepository.sendHumidity(sensorDO);

    verify(this.mqttClient).publish(eq("test"), argumentCaptor.capture());
    final MqttMessage result = argumentCaptor.getValue();
    assertThat(result, is(notNullValue()));
    assertThat(result.getQos(), is(1));
    assertThat(result.getPayload(), is(this.gson.toJson(sensorDO).getBytes()));
  }
}