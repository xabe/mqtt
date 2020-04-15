package com.xabe.mqtt.producer.infrastructure.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.xabe.mqtt.producer.domain.repository.ProducerRepository;
import com.xabe.mqtt.producer.infrastructure.application.ProducerUseCase;
import com.xabe.mqtt.producer.infrastructure.application.ProducerUseCaseImpl;
import com.xabe.mqtt.producer.infrastructure.messaging.ProducerRepositoryImpl;
import java.time.Clock;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

@ApplicationPath("/")
public class CustomResourceConfig extends ResourceConfig {

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
    this.packages("com.xabe.mqtt.producer.infrastructure.presentation");
    this.register(JacksonFeature.class);
    this.register(new LoggingFeature());
    this.register(new ObjectMapperContextResolver());
    this.register(new AbstractBinder() {
      @Override
      protected void configure() {
        this.bind(ProducerUseCaseImpl.class).to(ProducerUseCase.class).in(Singleton.class);
        this.bind(ProducerRepositoryImpl.class).to(ProducerRepository.class).in(Singleton.class);
        this.bind(mqttClient).to(IMqttClient.class);
        this.bind(config).to(Config.class);
        this.bind(Clock.systemUTC()).to(Clock.class);
      }
    });
    this.property(ServerProperties.BV_FEATURE_DISABLE, false);
    this.property(ServerProperties.RESOURCE_VALIDATION_IGNORE_ERRORS, true);
    this.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

  }
}
