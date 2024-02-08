package ovh.kg4.devicetracker.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ovh.kg4.devicetracker.dto.FonoapiDeviceDto;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.service.FonoapiService;

@Service
@Slf4j
public class FonoapiServiceImpl implements FonoapiService {

  @Value("${external.api.fonoapi.url}")
  private String fonoapiUrl;
  private final RestTemplate restTemplate;
  private final ObjectProvider<FonoapiService> proxy;


  public FonoapiServiceImpl(RestTemplate restTemplate, ObjectProvider<FonoapiService> proxy) {

    this.restTemplate = restTemplate;
    this.proxy = proxy;
  }

  @CircuitBreaker(label = "fonoapi-http-call-circuit-breaker",
    maxAttemptsExpression = "${spring.circuit.breaker.max.attempts:3}",
    openTimeoutExpression = "${spring.circuit.breaker.open.timeout:10000}",
    resetTimeoutExpression = "${spring.circuit.breaker.reset.timeout:60000}")
  public FonoapiDeviceDto pullDeviceInfo(Device device) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("device", device.getModelName());
    params.add("brand", device.getBrand());

    FonoapiDeviceDto[] response = proxy.getObject().callApi(
      new HttpEntity<>(params, headers)
    );

    if (response.length > 1) {
      log.warn("Ambiguous results {} for device '{} - {}'", response, device.getBrand(), device.getModelName());
    } else if (response.length == 0) {
      log.warn("Empty result for device '{} - {}'", device.getBrand(), device.getModelName());
    }

    return response.length > 0 ? response[0] : null;
  }

  @Retryable(maxAttemptsExpression = "${spring.retry.max.attempts:2}",
    backoff = @Backoff(delayExpression = "${spring.retry.backoff.delay:2000}"),
    retryFor = HttpServerErrorException.class)
  public FonoapiDeviceDto[] callApi(HttpEntity<MultiValueMap<String, String>> entity) {

    log.debug("Entity body '{}'", entity);
    return restTemplate.postForEntity(fonoapiUrl, entity, FonoapiDeviceDto[].class).getBody();
  }

  @Recover
  public FonoapiDeviceDto pullDeviceInfoRecover(Device device) {

    log.debug("Fallback scenario for fonoapi call");

    // TODO: Replace with a heuristic approach
    return device.getDeviceInfo() == null
      ? new FonoapiDeviceDto(device.getModelName(), device.getBrand(),
      "GSM", "GSM 900 / 1800", "No", "No")
      : null;
  }
}
