package com.xabe.mqtt.consumer.domain.repository;

import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import java.util.List;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface ConsumerRepository {

  List<SensorDO> getTemperatures();

  List<SensorDO> getHumidities();

  void addTemperature(SensorDO sensorDO);

  void addHumidity(SensorDO sensorDO);
}
