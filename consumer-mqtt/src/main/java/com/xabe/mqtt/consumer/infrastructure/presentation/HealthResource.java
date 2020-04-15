package com.xabe.mqtt.consumer.infrastructure.presentation;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Singleton
public class HealthResource {

  @GET
  @Path("/health")
  @Produces(MediaType.TEXT_PLAIN)
  public String healthCheck() {
    return "OK";
  }

}
