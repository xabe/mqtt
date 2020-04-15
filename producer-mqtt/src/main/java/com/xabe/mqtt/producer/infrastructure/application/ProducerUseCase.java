package com.xabe.mqtt.producer.infrastructure.application;

import com.xabe.mqtt.producer.infrastructure.presentation.payload.SensorPayload;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface ProducerUseCase {

  void sendSensorTemperature(SensorPayload sensorPayload);

  void sendSensorHumidity(SensorPayload sensorPayload);
}
