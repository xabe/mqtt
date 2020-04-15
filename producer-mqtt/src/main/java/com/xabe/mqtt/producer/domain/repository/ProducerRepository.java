package com.xabe.mqtt.producer.domain.repository;

import com.xabe.mqtt.producer.domain.entity.SensorDO;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface ProducerRepository {

  Void sendTemperature(SensorDO sensorDO);

  Void sendHumidity(SensorDO sensorDO);
}
