package com.xabe.mqtt.producer.infrastructure.application;

import com.xabe.mqtt.producer.domain.entity.SensorDO;
import com.xabe.mqtt.producer.domain.entity.Type;
import com.xabe.mqtt.producer.domain.repository.ProducerRepository;
import com.xabe.mqtt.producer.infrastructure.presentation.payload.SensorPayload;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class ProducerUseCaseImpl implements ProducerUseCase {

  private final ProducerRepository producerRepository;

  private final Map<Type, Function<SensorDO, Void>> map;

  @Inject
  public ProducerUseCaseImpl(final ProducerRepository producerRepository) {
    this.producerRepository = producerRepository;
    this.map = new HashMap<>();
    this.map.put(Type.TEMPERATURE, producerRepository::sendTemperature);
    this.map.put(Type.HUMIDITY, producerRepository::sendHumidity);
  }

  @Override
  public void sendSensorTemperature(final SensorPayload sensorPayload) {
    this.map.get(Type.TEMPERATURE).apply(this.mapper(sensorPayload));
  }

  @Override
  public void sendSensorHumidity(final SensorPayload sensorPayload) {
    this.map.get(Type.HUMIDITY).apply(this.mapper(sensorPayload));
  }

  private SensorDO mapper(final SensorPayload payload) {
    return SensorDO.builder().unit(payload.getUnit().name()).value(payload.getValue()).build();
  }
}
