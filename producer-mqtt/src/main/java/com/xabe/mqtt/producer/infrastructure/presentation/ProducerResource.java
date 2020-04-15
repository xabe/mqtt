package com.xabe.mqtt.producer.infrastructure.presentation;

import com.xabe.mqtt.producer.infrastructure.application.ProducerUseCase;
import com.xabe.mqtt.producer.infrastructure.presentation.payload.SensorPayload;
import java.time.Clock;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/producer/sensor")
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ProducerResource {

  private final Logger logger = LoggerFactory.getLogger(ProducerResource.class);

  private final ProducerUseCase producerUseCase;

  private final Clock clock;

  @POST
  @Path("/temperature")
  public Response createSensorTemperature(@Valid final SensorPayload sensorPayload) {
    final long timestamp = sensorPayload.getTimestamp() <= 0 ? this.clock.millis() : sensorPayload.getTimestamp();
    this.producerUseCase.sendSensorTemperature(sensorPayload.toBuilder().timestamp(timestamp).build());
    this.logger.info("send sensor temperature {}", sensorPayload);
    return Response.noContent().build();
  }

  @POST
  @Path("/humidity")
  public Response createSensorHumidity(@Valid final SensorPayload sensorPayload) {
    final long timestamp = sensorPayload.getTimestamp() <= 0 ? this.clock.millis() : sensorPayload.getTimestamp();
    this.producerUseCase.sendSensorHumidity(sensorPayload.toBuilder().timestamp(timestamp).build());
    this.logger.info("send sensor humidity {}", sensorPayload);
    return Response.noContent().build();
  }

}
