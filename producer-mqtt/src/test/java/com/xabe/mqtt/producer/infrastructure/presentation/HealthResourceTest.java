package com.xabe.mqtt.producer.infrastructure.presentation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HealthResourceTest {

  private HealthResource healthResource;

  @BeforeEach
  public void setUp() throws Exception {
      this.healthResource = new HealthResource();
  }

  @Test
  public void shouldGetStatus() throws Exception {

    final String result = this.healthResource.healthCheck();

    assertThat(result, is(notNullValue()));
  }
}