package com.xabe.mqtt.producer.infrastructure.application;

import com.xabe.mqtt.producer.infrastructure.presentation.payload.SensorPayload;

public interface ProducerUseCase {

  void sendSensorTemperature(SensorPayload sensorPayload);

  void sendSensorHumidity(SensorPayload sensorPayload);
}
