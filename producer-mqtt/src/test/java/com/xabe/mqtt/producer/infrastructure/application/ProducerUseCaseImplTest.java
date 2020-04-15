package com.xabe.mqtt.producer.infrastructure.application;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.xabe.mqtt.producer.domain.entity.SensorDO;
import com.xabe.mqtt.producer.domain.repository.ProducerRepository;
import com.xabe.mqtt.producer.infrastructure.presentation.payload.SensorPayload;
import com.xabe.mqtt.producer.infrastructure.presentation.payload.UnitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProducerUseCaseImplTest {

  private ProducerRepository producerRepository;

  private ProducerUseCase producerUseCase;

  @BeforeEach
  public void setUp() throws Exception {
    this.producerRepository = mock(ProducerRepository.class);
    this.producerUseCase = new ProducerUseCaseImpl(this.producerRepository);
  }

  @Test
  public void sendSensorTemperature() throws Exception {
    final SensorPayload sensorPayload = SensorPayload.builder().unit(UnitType.FAHRENHEIT).value("3").timestamp(2L).build();

    this.producerUseCase.sendSensorTemperature(sensorPayload);

    verify(this.producerRepository).sendTemperature(eq(SensorDO.builder().value("3").unit("FAHRENHEIT").timestamp(2L).build()));
  }

  @Test
  public void sendSensorHumidity() throws Exception {
    final SensorPayload sensorPayload = SensorPayload.builder().unit(UnitType.FAHRENHEIT).value("3").timestamp(4L).build();

    this.producerUseCase.sendSensorHumidity(sensorPayload);

    verify(this.producerRepository).sendHumidity(eq(SensorDO.builder().value("3").unit("FAHRENHEIT").timestamp(4L).build()));
  }

}