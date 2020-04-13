package com.xabe.mqtt.producer.infrastructure.presentation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.xabe.mqtt.producer.infrastructure.application.ProducerUseCase;
import com.xabe.mqtt.producer.infrastructure.presentation.payload.SensorPayload;
import com.xabe.mqtt.producer.infrastructure.presentation.payload.UnitType;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProducerResourceTest {

  private ProducerUseCase producerUseCase;

  private ProducerResource producerResource;

  @BeforeEach
  public void setUp() throws Exception {
    this.producerUseCase = mock(ProducerUseCase.class);
    this.producerResource = new ProducerResource(this.producerUseCase);
  }

  @Test
  public void sendSensorTemperature() throws Exception {
    final SensorPayload sensorPayload = SensorPayload.builder().value("5").unit(UnitType.FAHRENHEIT).build();

    final Response result = this.producerResource.createSensorTemperature(sensorPayload);

    assertThat(result, is(notNullValue()));
    assertThat(result.getStatus(), is(204));
    verify(this.producerUseCase).sendSensorTemperature(eq(sensorPayload));
  }

  @Test
  public void sendSensorHumidity() throws Exception {
    final SensorPayload sensorPayload = SensorPayload.builder().value("5").unit(UnitType.FAHRENHEIT).build();

    final Response result = this.producerResource.createSensorHumidity(sensorPayload);

    assertThat(result, is(notNullValue()));
    assertThat(result.getStatus(), is(204));
    verify(this.producerUseCase).sendSensorHumidity(eq(sensorPayload));
  }

}