package com.xabe.mqtt.consumer.infrastructure.config;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

public class ImmediateFeature implements Feature {

  @Inject
  public ImmediateFeature(final ServiceLocator locator) {
    ServiceLocatorUtilities.enableImmediateScope(locator);
  }

  @Override
  public boolean configure(final FeatureContext context) {
    return true;
  }
}
