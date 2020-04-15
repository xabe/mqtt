package com.xabe.mqtt.consumer.infrastructure.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import com.xabe.mqtt.consumer.domain.repository.ConsumerRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryConsumerRepositoryTest {

  private ConsumerRepository consumerRepository;

  @BeforeEach
  public void setUp() throws Exception {
    this.consumerRepository = new InMemoryConsumerRepository();
  }

  @Test
  public void shouldAddSensorTemperature() throws Exception {
    final SensorDO sensorDO = SensorDO.builder().build();

    this.consumerRepository.addTemperature(sensorDO);

    final List<SensorDO> result = this.consumerRepository.getTemperatures();

    assertThat(result, is(notNullValue()));
    assertThat(result, is(hasSize(1)));
    assertThat(result, is(hasItem(sensorDO)));
  }

  @Test
  public void shouldAddSensorHumidity() throws Exception {
    final SensorDO sensorDO = SensorDO.builder().build();

    this.consumerRepository.addHumidity(sensorDO);

    final List<SensorDO> result = this.consumerRepository.getHumidities();

    assertThat(result, is(notNullValue()));
    assertThat(result, is(hasSize(1)));
    assertThat(result, is(hasItem(sensorDO)));
  }

}