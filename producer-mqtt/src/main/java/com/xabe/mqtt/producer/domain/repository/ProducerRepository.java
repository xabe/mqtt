package com.xabe.mqtt.producer.domain.repository;

import com.xabe.mqtt.producer.domain.entity.SensorDO;

public interface ProducerRepository {

  Void sendTemperature(SensorDO sensorDO);

  Void sendHumidity(SensorDO sensorDO);
}
