package com.xabe.mqtt.consumer.infrastructure.messaging.handler;

import static com.xabe.mqtt.consumer.infrastructure.messaging.MqttProcessor.MQTT_HUMIDITY_TOPIC;

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
@Named("humidity")
public class HumidityHandler extends AbstractHandler<SensorDO> {

  @Inject
  public HumidityHandler(final ConsumerRepository consumerRepository, final ObjectMapper objectMapper, final Config config) {
    super(objectMapper, config.getString(MQTT_HUMIDITY_TOPIC), SensorDO.class, consumerRepository::addHumidity);
  }

}
