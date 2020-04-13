package com.xabe.mqtt.producer.infrastructure.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.google.gson.Gson;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.xabe.mqtt.producer.infrastructure.messaging.ProducerRepositoryImpl;
import com.xabe.mqtt.producer.infrastructure.presentation.payload.SensorPayload;
import com.xabe.mqtt.producer.infrastructure.presentation.payload.UnitType;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class EventsProcessingIT {

  private static final long DEFAULT_TIMEOUT = 3000;

  private final Config config = ConfigFactory.load();

  private final Gson gson = new Gson();

  private final MqttProcessor mqttProcessor = new MqttProcessor(this.config);

  @BeforeEach
  public void init() {
    this.mqttProcessor.before();
  }

  @Test
  public void shouldSensorTemperature() throws Exception {
    final SensorPayload sensorPayload = SensorPayload.builder().value("10").unit(UnitType.CELSIUS).build();

    final HttpResponse<JsonNode> response = Unirest
        .post(String.format("http://localhost:%d/producer/sensor/temperature", this.config.getInt("server.port")))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(sensorPayload).asJson();

    assertThat(response, is(notNullValue()));
    assertThat(response.getStatus(), is(204));

    final Message result = this.mqttProcessor.expectMessagePipe(DEFAULT_TIMEOUT);
    assertThat(result, is(notNullValue()));
    assertThat(result.getTopic(), is(this.config.getString(ProducerRepositoryImpl.MQTT_TEMPERATURE_TOPIC)));
    assertThat(new String(result.getMqttMessage().getPayload(), StandardCharsets.UTF_8), is(this.gson.toJson(sensorPayload)));
  }

  @Test
  public void shouldSensorHumidity() throws Exception {
    final SensorPayload sensorPayload = SensorPayload.builder().value("50").unit(UnitType.PERCENTAGE).build();

    final HttpResponse<JsonNode> response = Unirest
        .post(String.format("http://localhost:%d/producer/sensor/humidity", this.config.getInt("server.port")))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(sensorPayload).asJson();

    assertThat(response, is(notNullValue()));
    assertThat(response.getStatus(), is(204));

    final Message result = this.mqttProcessor.expectMessagePipe(DEFAULT_TIMEOUT);
    assertThat(result, is(notNullValue()));
    assertThat(result.getTopic(), is(this.config.getString(ProducerRepositoryImpl.MQTT_HUMIDITY_TOPIC)));
    assertThat(new String(result.getMqttMessage().getPayload(), StandardCharsets.UTF_8), is(this.gson.toJson(sensorPayload)));
  }

}
