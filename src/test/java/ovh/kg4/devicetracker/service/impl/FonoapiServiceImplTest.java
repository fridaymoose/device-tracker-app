package ovh.kg4.devicetracker.service.impl;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ovh.kg4.devicetracker.DeviceTrackerApplication;
import ovh.kg4.devicetracker.dto.FonoapiDeviceDto;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.entity.DeviceInfo;
import ovh.kg4.devicetracker.service.FonoapiService;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = DeviceTrackerApplication.class)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8909)
class FonoapiServiceImplTest {

  @Value("${spring.circuit.breaker.reset.timeout}")
  Integer circuitBreakerResetTimeout;

  @SpyBean
  FonoapiService fonoapiService;

  @Test
  void pullDeviceInfo() {

    Device device = new Device();
    device.setModelName("Nokia 3310");
    device.setBrand("Nokia");

    FonoapiDeviceDto fonoapiDeviceDto = fonoapiService.pullDeviceInfo(device);

    verify(fonoapiService, times(1)).pullDeviceInfo(device);
    verify(fonoapiService, times(1)).callApi(any());

    assertEquals("Nokia", fonoapiDeviceDto.brand());
    assertEquals("Nokia 3310", fonoapiDeviceDto.deviceName());
    assertEquals("GSM", fonoapiDeviceDto.technology());
    assertEquals("GSM 900 / 1800", fonoapiDeviceDto.bands2G());
    assertEquals("No", fonoapiDeviceDto.bands3G());
    assertEquals("No", fonoapiDeviceDto.bands4G());
  }

  @Test
  void pullDeviceInfo401() throws InterruptedException {

    Device device = new Device();
    device.setBrand("Nokia");
    device.setModelName("http 401");

    IntStream.range(0, 4).forEach(i -> fonoapiService.pullDeviceInfo(device));

    verify(fonoapiService, times(3)).pullDeviceInfo(device);
    verify(fonoapiService, times(4)).pullDeviceInfoRecover(device);

    Thread.sleep(circuitBreakerResetTimeout);
  }

  @Test
  void pullDeviceInfo503() throws InterruptedException {

    Device device = new Device();
    device.setBrand("Nokia");
    device.setModelName("http 503");

    IntStream.range(0, 4).forEach(i -> fonoapiService.pullDeviceInfo(device));

    verify(fonoapiService, times(3)).pullDeviceInfo(device);
    verify(fonoapiService, times(4)).pullDeviceInfoRecover(device);

    Thread.sleep(circuitBreakerResetTimeout);
  }

  @Test
  void callApi() {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("brand", "Nokia");
    params.add("device", "Nokia 3310");

    var httpEntity = new HttpEntity<>(params, headers);

    FonoapiDeviceDto[] payload = fonoapiService.callApi(httpEntity);
    assertEquals(1, payload.length);
  }

  @Test
  void callApi503() {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("brand", "Nokia");
    params.add("device", "http 503");

    var httpEntity = new HttpEntity<>(params, headers);

    Exception exception = assertThrows(ExhaustedRetryException.class,
      () -> fonoapiService.callApi(httpEntity)
    );

    verify(fonoapiService, times(2)).callApi(httpEntity);

    assertTrue(exception.getMessage().contains("Cannot locate recovery method"));
    assertTrue(exception.getCause().getMessage().contains("503 Service Unavailable"));
  }

  @Test
  void callApi401() {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("brand", "Nokia");
    params.add("device", "http 401");

    var httpEntity = new HttpEntity<>(params, headers);

    Exception exception = assertThrows(ExhaustedRetryException.class,
      () -> fonoapiService.callApi(httpEntity)
    );

    verify(fonoapiService, times(1)).callApi(httpEntity);

    assertTrue(exception.getMessage().contains("Cannot locate recovery method"));
    assertTrue(exception.getCause().getMessage().contains("401 Unauthorized"));
  }

  @Test
  void pullDeviceInfoRecoverNoDeviceInfo() {

    Device device = new Device();
    device.setBrand("Nokia");
    device.setModelName("Nokia 3310");

    FonoapiDeviceDto fonoapiDeviceDto = fonoapiService.pullDeviceInfoRecover(device);
    assertEquals("GSM", fonoapiDeviceDto.technology());
    assertEquals("GSM 900 / 1800", fonoapiDeviceDto.bands2G());
    assertEquals("No", fonoapiDeviceDto.bands3G());
    assertEquals("No", fonoapiDeviceDto.bands4G());
  }

  @Test
  void pullDeviceInfoRecover() {

    Device galaxyS9 = new Device();
    galaxyS9.setId(1L);
    galaxyS9.setBrand("Samsung");
    galaxyS9.setModelName("Samsung Galaxy S9");

    DeviceInfo galaxyS9Info = new DeviceInfo();
    galaxyS9Info.setTechnology("GSM / CDMA / HSPA / EVDO / LTE");
    galaxyS9Info.setBands2G("GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)");
    galaxyS9Info.setBands3G("HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100 - Global, USA");
    galaxyS9Info.setBands4G("1, 2, 3, 4, 5, 7, 8, 12, 13, 17, 18, 19, 20, 25, 26, 28, 32, 38, 39, 40, 41, 66 - Global");

    galaxyS9.setDeviceInfo(galaxyS9Info);

    FonoapiDeviceDto fonoapiDeviceDto = fonoapiService.pullDeviceInfoRecover(galaxyS9);
    assertNull(fonoapiDeviceDto);
  }
}