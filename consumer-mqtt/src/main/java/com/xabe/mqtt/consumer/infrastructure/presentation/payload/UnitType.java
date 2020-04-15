package com.xabe.mqtt.consumer.infrastructure.presentation.payload;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public enum UnitType {
  CELSIUS,
  FAHRENHEIT,
  PERCENTAGE;

  public static final Map<String, UnitType> MAP = Stream.of(UnitType.values())
      .collect(Collectors.collectingAndThen(Collectors.toMap(UnitType::name, Function.identity()), Collections::unmodifiableMap));

  public static UnitType getUnit(final String unit) {
    return StringUtils.isBlank(unit) ? null : MAP.get(unit.toUpperCase());
  }
}
