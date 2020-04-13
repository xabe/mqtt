package com.xabe.mqtt.producer.infrastructure.integration;

import com.typesafe.config.Config;
import com.xabe.mqtt.producer.infrastructure.messaging.ProducerRepositoryImpl;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttProcessor implements MqttCallback {

  private final BlockingQueue<Message> messagesPipe = new ArrayBlockingQueue<>(100);

  private final Logger logger = LoggerFactory.getLogger(MqttProcessor.class);

  public MqttProcessor(final Config config) {
    final IMqttClient mqttClient;
    try {
      mqttClient = new MqttClient(config.getString("mqtt.temperature.broker"), "test-producer", new MemoryPersistence());
      final MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      connOpts.setKeepAliveInterval(30);
      connOpts.setUserName(config.getString("mqtt.temperature.user"));
      connOpts.setPassword(config.getString("mqtt.temperature.pass").toCharArray());
      mqttClient.connect(connOpts);
      mqttClient.setCallback(this);
      mqttClient.subscribe(config.getString(ProducerRepositoryImpl.MQTT_TEMPERATURE_TOPIC));
      mqttClient.subscribe(config.getString(ProducerRepositoryImpl.MQTT_HUMIDITY_TOPIC));
      this.logger.info("Create MqttProcessor");
    } catch (final MqttException e) {
      this.logger.error("Error create MqttProcessor ", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void messageArrived(final String topic, final MqttMessage mqttMessage) throws Exception {
    this.processMessage(Message.builder().topic(topic).mqttMessage(mqttMessage).build());
  }

  private void processMessage(final Message message) throws InterruptedException {
    this.logger.info("Event received from topic {} and message {}", message.getTopic(), message.getMqttMessage());
    if (!this.messagesPipe.offer(message, 1, TimeUnit.SECONDS)) {
      this.logger.warn("Adding {} to messagesPipe timed out", message);
    }
  }

  public void before() {
    this.messagesPipe.clear();
  }

  public <T> Message expectMessagePipe(final long milliseconds) throws InterruptedException {
    final Message message = this.messagesPipe.poll(milliseconds, TimeUnit.MILLISECONDS);
    if (message == null) {
      throw new RuntimeException("An exception happened while polling the queue ");
    }
    return message;
  }

  public <T> List<Message> expectMultipleMessagesPipe(final long milliseconds, final int size) throws InterruptedException {
    return IntStream.range(0, size).mapToObj(item -> {
      try {
        return this.expectMessagePipe(milliseconds);
      } catch (InterruptedException e) {
        throw new RuntimeException(e.getCause());
      }
    }).collect(Collectors.toList());
  }

  @Override
  public void connectionLost(final Throwable throwable) {

  }

  @Override
  public void deliveryComplete(final IMqttDeliveryToken iMqttDeliveryToken) {

  }
}
