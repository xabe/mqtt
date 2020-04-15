package com.xabe.mqtt.consumer.infrastructure.application;

import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import java.util.List;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface ConsumerUseCase {

  List<SensorDO> getTemperatures();

  List<SensorDO> getHumidities();
}
