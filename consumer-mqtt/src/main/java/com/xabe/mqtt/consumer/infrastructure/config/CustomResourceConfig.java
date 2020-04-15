package com.xabe.mqtt.consumer.infrastructure.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.xabe.mqtt.consumer.domain.repository.ConsumerRepository;
import com.xabe.mqtt.consumer.infrastructure.application.ConsumerUseCase;
import com.xabe.mqtt.consumer.infrastructure.application.ConsumerUseCaseImpl;
import com.xabe.mqtt.consumer.infrastructure.messaging.handler.HumidityHandler;
import com.xabe.mqtt.consumer.infrastructure.messaging.MqttHandler;
import com.xabe.mqtt.consumer.infrastructure.messaging.MqttProcessor;
import com.xabe.mqtt.consumer.infrastructure.messaging.handler.TemperatureHandler;
import com.xabe.mqtt.consumer.infrastructure.persistence.InMemoryConsumerRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.glassfish.hk2.api.Immediate;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

@ApplicationPath("/")
public class CustomResourceConfig extends ResourceConfig {

  /**
   * How use @Immediate https://stackoverflow.com/questions/28114602 https://stackoverflow.com/questions/29289245
   * https://stackoverflow.com/questions/23402814
   */
  @Inject
  public CustomResourceConfig() throws MqttException {
    final Config config = ConfigFactory.load();
    final IMqttClient mqttClient = new MqttClient(config.getString("mqtt.temperature.broker"),
        config.getString("mqtt.temperature.clientId"), new MemoryPersistence());
    final MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(true);
    connOpts.setKeepAliveInterval(30);
    connOpts.setUserName(config.getString("mqtt.temperature.user"));
    connOpts.setPassword(config.getString("mqtt.temperature.pass").toCharArray());
    mqttClient.connect(connOpts);
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    objectMapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    this.packages(true, "com.xabe.mqtt.consumer.infrastructure.presentation");
    this.register(JacksonFeature.class);
    this.register(new LoggingFeature());
    this.register(new ObjectMapperContextResolver());
    this.register(ImmediateFeature.class);
    this.register(new AbstractBinder() {
      @Override
      protected void configure() {
        this.bind(ConsumerUseCaseImpl.class).to(ConsumerUseCase.class).in(Singleton.class);
        this.bind(InMemoryConsumerRepository.class).to(ConsumerRepository.class).in(Singleton.class);
        this.bind(mqttClient).to(IMqttClient.class);
        this.bind(config).to(Config.class);
        this.bind(objectMapper).to(ObjectMapper.class);
        this.bind(MqttProcessor.class).to(MqttProcessor.class).in(Immediate.class);
        this.bind(HumidityHandler.class).to(MqttHandler.class).in(Singleton.class);
        this.bind(TemperatureHandler.class).to(MqttHandler.class).in(Singleton.class);
      }
    });
    this.property(ServerProperties.BV_FEATURE_DISABLE, false);
    this.property(ServerProperties.RESOURCE_VALIDATION_IGNORE_ERRORS, true);
    this.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

  }
}
