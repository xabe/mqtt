package com.xabe.mqtt.consumer.infrastructure.presentation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import com.xabe.mqtt.consumer.infrastructure.application.ConsumerUseCase;
import com.xabe.mqtt.consumer.infrastructure.presentation.payload.SensorPayload;
import com.xabe.mqtt.consumer.infrastructure.presentation.payload.UnitType;
import java.time.Instant;
import java.util.Collections;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumerResourceTest {

  private ConsumerUseCase consumerUseCase;

  private ConsumerResource consumerResource;

  @BeforeEach
  public void setUp() throws Exception {
    this.consumerUseCase = mock(ConsumerUseCase.class);
    this.consumerResource = new ConsumerResource(this.consumerUseCase);
  }

  @Test
  public void shouldGetAllSensorTemperature() throws Exception {
    final Instant instant = Instant.now();
    when(this.consumerUseCase.getTemperatures())
        .thenReturn(Collections.singletonList(SensorDO.builder().unit("CELSIUS").value("10").timestamp(instant.toEpochMilli()).build()));

    final Response result = this.consumerResource.getSensorTemperature();

    assertThat(result, is(notNullValue()));
    assertThat(result.getStatus(), is(HttpStatus.SC_OK));
    assertThat(result.getEntity(),
        is(Collections.singletonList(SensorPayload.builder().unit(UnitType.CELSIUS).value("10").instant(Instant.ofEpochMilli(instant.toEpochMilli())).build())));
  }

  @Test
  public void shouldGetAllSensorHumidity() throws Exception {
    final Instant instant = Instant.now();
    when(this.consumerUseCase.getHumidities())
        .thenReturn(Collections.singletonList(SensorDO.builder().unit("CELSIUS").value("10").timestamp(instant.toEpochMilli()).build()));

    final Response result = this.consumerResource.getSensorHumidity();

    assertThat(result, is(notNullValue()));
    assertThat(result.getStatus(), is(HttpStatus.SC_OK));
    assertThat(result.getEntity(),
        is(Collections.singletonList(SensorPayload.builder().unit(UnitType.CELSIUS).value("10").instant(Instant.ofEpochMilli(instant.toEpochMilli())).build())));
  }

}