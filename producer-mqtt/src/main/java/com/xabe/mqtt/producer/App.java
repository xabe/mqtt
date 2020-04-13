package com.xabe.mqtt.producer;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.xabe.mqtt.producer.infrastructure.config.CustomResourceConfig;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.LogManager;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class App {

  private static final String BIND_IP = "0.0.0.0";

  private static HttpServer server;

  public static void main(final String[] args) throws InterruptedException, IOException, MqttException {
    final Config config = ConfigFactory.load();
    LogManager.getLogManager().reset();
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
    java.util.logging.Logger.getLogger("global").setLevel(Level.ALL);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> server.shutdownNow()));
    final ResourceConfig rc = new CustomResourceConfig();
    server = GrizzlyHttpServerFactory.createHttpServer(URI.create(getUriInfo("http", config.getInt("server.port"))), rc, false);
    server.getServerConfiguration().setAllowPayloadForUndefinedHttpMethods(true);
    server.start();
    LoggerFactory.getLogger(App.class).info("Stop the application using CTRL+C");
    Thread.currentThread().join();
  }

  private static String getUriInfo(final String protocol, final int port) {
    return String.format("%s://%s:%d", protocol, BIND_IP, port);
  }

}
