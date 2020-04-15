package com.xabe.mqtt.consumer.infrastructure.messaging.handler;

import static com.xabe.mqtt.consumer.infrastructure.messaging.MqttProcessor.MQTT_TEMPERATURE_TOPIC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import com.xabe.mqtt.consumer.domain.repository.ConsumerRepository;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jvnet.hk2.annotations.Service;

@Service
@Singleton
@Named("temperature")
public class TemperatureHandler extends AbstractHandler<SensorDO> {

  @Inject
  public TemperatureHandler(final ConsumerRepository consumerRepository, final ObjectMapper objectMapper, final Config config) {
    super(objectMapper, config.getString(MQTT_TEMPERATURE_TOPIC), SensorDO.class, consumerRepository::addTemperature);
  }

}
