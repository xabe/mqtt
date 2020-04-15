package com.xabe.mqtt.consumer.infrastructure.persistence;

import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import com.xabe.mqtt.consumer.domain.repository.ConsumerRepository;
import java.util.LinkedList;
import java.util.List;
import org.glassfish.hk2.api.Immediate;
import org.jvnet.hk2.annotations.Service;

@Service
@Immediate
public class InMemoryConsumerRepository implements ConsumerRepository {

  private final List<SensorDO> temperatures;

  private final List<SensorDO> humidities;

  public InMemoryConsumerRepository() {
    this.humidities = new LinkedList<>();
    this.temperatures = new LinkedList<>();
  }

  @Override
  public void addTemperature(final SensorDO sensorDO) {
    this.temperatures.add(sensorDO);
  }

  @Override
  public void addHumidity(final SensorDO sensorDO) {
    this.humidities.add(sensorDO);
  }

  @Override
  public List<SensorDO> getHumidities() {
    return this.humidities;
  }

  @Override
  public List<SensorDO> getTemperatures() {
    return this.temperatures;
  }
}
