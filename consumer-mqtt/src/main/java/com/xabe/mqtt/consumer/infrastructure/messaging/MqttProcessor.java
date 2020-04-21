package com.xabe.mqtt.consumer.infrastructure.messaging;

import com.typesafe.config.Config;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.glassfish.hk2.api.Immediate;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Immediate
public class MqttProcessor implements MqttCallback {

  public static final String MQTT_TEMPERATURE_TOPIC = "mqtt.temperature.topic";

  public static final String MQTT_HUMIDITY_TOPIC = "mqtt.humidity.topic";

  private static final Logger logger = LoggerFactory.getLogger(MqttProcessor.class);

  private static final MqttHandler UNKNOWN = new MqttHandler() {
    @Override
    public String getTopic() {
      return null;
    }

    @Override
    public void handler(final MqttMessage mqttMessage) {
      logger.warn("Not handler for message {}", mqttMessage);
    }
  };

  private final IMqttClient mqttClient;

  private final Config config;

  private final Map<String, MqttHandler> handler;

  @Inject
  public MqttProcessor(final IMqttClient mqttClient, final Config config, final Iterable<MqttHandler> mqttHandlers) throws MqttException {
    this.mqttClient = mqttClient;
    this.config = config;
    this.mqttClient.setCallback(this);
    this.mqttClient.subscribe(config.getString(MQTT_TEMPERATURE_TOPIC));
    this.mqttClient.subscribe(config.getString(MQTT_HUMIDITY_TOPIC));
    this.handler = StreamSupport.stream(mqttHandlers.spliterator(), false)
        .collect(Collectors.toMap(MqttHandler::getTopic, Function.identity()));
    this.logger.info("MqttProcessor listener");
  }

  @Override
  public void connectionLost(final Throwable throwable) {

  }

  @Override
  public void messageArrived(final String topic, final MqttMessage mqttMessage) throws Exception {
    this.handler.getOrDefault(topic, UNKNOWN).handler(mqttMessage);

  }

  @Override
  public void deliveryComplete(final IMqttDeliveryToken iMqttDeliveryToken) {

  }

  @PreDestroy
  public void dispose() throws MqttException {
    this.mqttClient.disconnect();
  }
}
