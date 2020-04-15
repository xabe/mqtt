package com.xabe.mqtt.consumer.infrastructure.messaging;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface MqttHandler {

  String getTopic();

  void handler(MqttMessage mqttMessage) throws IOException;

}
