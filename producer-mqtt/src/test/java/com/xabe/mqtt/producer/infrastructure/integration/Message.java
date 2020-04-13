package com.xabe.mqtt.producer.infrastructure.integration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Message {

  private String topic;

  private MqttMessage mqttMessage;

}
