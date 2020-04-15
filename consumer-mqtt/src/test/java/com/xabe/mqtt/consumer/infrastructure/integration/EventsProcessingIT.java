package com.xabe.mqtt.consumer.infrastructure.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.GsonBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.xabe.mqtt.consumer.infrastructure.presentation.payload.SensorPayload;
import com.xabe.mqtt.consumer.infrastructure.presentation.payload.UnitType;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import kong.unirest.HttpResponse;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.gson.GsonObjectMapper;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class EventsProcessingIT {

  public static final int TIMEOUT_MS = 3000;

  public static final int DELAY_MS = 500;

  public static final int POLL_INTERVAL_MS = 500;

  private final Config config = ConfigFactory.load();

  private final ObjectMapper objectMapper = new GsonObjectMapper(Converters.registerInstant(new GsonBuilder()).create());

  private final MqttProducer mqttProducer = new MqttProducer(this.config);

  @Test
  public void shouldSensorTemperature() throws Exception {
    final SensorPayload sensorPayload = SensorPayload.builder().value("10").unit(UnitType.CELSIUS).build();

    this.mqttProducer.sendSensor(sensorPayload, "mqtt.temperature.topic");

    Awaitility.await().pollDelay(DELAY_MS, TimeUnit.MILLISECONDS).pollInterval(POLL_INTERVAL_MS, TimeUnit.MILLISECONDS)
        .atMost(TIMEOUT_MS, TimeUnit.MILLISECONDS).until(() -> {

      final HttpResponse<SensorPayload[]> response = Unirest
          .get(String.format("http://localhost:%d/consumer/sensor/temperature", this.config.getInt("server.port")))
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).withObjectMapper(this.objectMapper).asObject(SensorPayload[].class);

      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus(), is(200));
      assertThat(response.getBody().length, is(greaterThanOrEqualTo(1)));
      return true;
    });
  }

  @Test
  public void shouldSensorHumidity() throws Exception {
    final SensorPayload sensorPayload = SensorPayload.builder().value("10").unit(UnitType.PERCENTAGE).build();

    this.mqttProducer.sendSensor(sensorPayload, "mqtt.humidity.topic");

    Awaitility.await().pollDelay(DELAY_MS, TimeUnit.MILLISECONDS).pollInterval(POLL_INTERVAL_MS, TimeUnit.MILLISECONDS)
        .atMost(TIMEOUT_MS, TimeUnit.MILLISECONDS).until(() -> {

      final HttpResponse<SensorPayload[]> response = Unirest
          .get(String.format("http://localhost:%d/consumer/sensor/humidity", this.config.getInt("server.port")))
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).withObjectMapper(this.objectMapper).asObject(SensorPayload[].class);

      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus(), is(200));
      assertThat(response.getBody().length, is(greaterThanOrEqualTo(1)));
      return true;
    });
  }

}
