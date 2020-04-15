package com.xabe.mqtt.consumer.infrastructure.application;

import com.xabe.mqtt.consumer.domain.entity.SensorDO;
import com.xabe.mqtt.consumer.domain.repository.ConsumerRepository;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;

@Service
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ConsumerUseCaseImpl implements ConsumerUseCase {

  private final ConsumerRepository consumerRepository;

  @Override
  public List<SensorDO> getTemperatures() {
    return this.consumerRepository.getTemperatures();
  }

  @Override
  public List<SensorDO> getHumidities() {
    return this.consumerRepository.getHumidities();
  }
}
