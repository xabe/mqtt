package com.xabe.mqtt.consumer.infrastructure.presentation;

import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import com.xabe.mqtt.consumer.infrastructure.application.ConsumerUseCase;
import com.xabe.mqtt.consumer.infrastructure.presentation.payload.SensorPayload;
import com.xabe.mqtt.consumer.infrastructure.presentation.payload.UnitType;
import java.time.Instant;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/consumer/sensor")
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ConsumerResource {

  private final ConsumerUseCase producerUseCase;

  @GET
  @Path("/temperature")
  public Response getSensorTemperature() {
    return Response.ok(this.producerUseCase.getTemperatures().stream().map(this::mapper).collect(Collectors.toList())).build();
  }

  @GET
  @Path("/humidity")
  public Response getSensorHumidity() {
    return Response.ok(this.producerUseCase.getHumidities().stream().map(this::mapper).collect(Collectors.toList())).build();
  }

  private SensorPayload mapper(final SensorDO sensorDO) {
    return SensorPayload.builder().value(sensorDO.getValue()).unit(UnitType.getUnit(sensorDO.getUnit()))
        .instant(Instant.ofEpochMilli(sensorDO.getTimestamp())).build();
  }
}
