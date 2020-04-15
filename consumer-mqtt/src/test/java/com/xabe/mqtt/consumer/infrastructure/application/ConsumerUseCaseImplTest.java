package com.xabe.mqtt.consumer.infrastructure.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import com.xabe.mqtt.consumer.domain.repository.ConsumerRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumerUseCaseImplTest {

  private ConsumerRepository consumerRepository;

  private ConsumerUseCase consumerUseCase;

  @BeforeEach
  public void setUp() throws Exception {
    this.consumerRepository = mock(ConsumerRepository.class);
    this.consumerUseCase = new ConsumerUseCaseImpl(this.consumerRepository);
  }

  @Test
  public void shouldGetSensorTemperature() throws Exception {

    when(this.consumerRepository.getTemperatures()).thenReturn(Collections.singletonList(SensorDO.builder().build()));

    final List<SensorDO> result = this.consumerUseCase.getTemperatures();

    assertThat(result, is(notNullValue()));
  }

  @Test
  public void shouldGetSensorHumidity() throws Exception {

    when(this.consumerRepository.getHumidities()).thenReturn(Collections.singletonList(SensorDO.builder().build()));

    final List<SensorDO> result = this.consumerUseCase.getHumidities();

    assertThat(result, is(notNullValue()));
  }
}