package ovh.kg4.devicetracker.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import ovh.kg4.devicetracker.dto.FonoapiDeviceDto;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.entity.DeviceInfo;
import ovh.kg4.devicetracker.repository.DeviceInfoRepo;
import ovh.kg4.devicetracker.repository.DeviceRepo;
import ovh.kg4.devicetracker.service.DeviceService;
import ovh.kg4.devicetracker.service.FonoapiService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {
  private final DeviceRepo deviceRepo;
  private final DeviceInfoRepo deviceInfoRepo;
  private final FonoapiService fonoapiService;

  public DeviceServiceImpl(DeviceRepo deviceRepo, DeviceInfoRepo deviceInfoRepo, FonoapiService fonoapiService) {

    this.deviceRepo = deviceRepo;
    this.deviceInfoRepo = deviceInfoRepo;
    this.fonoapiService = fonoapiService;
  }

  @Override
  public Optional<Device> getId(Long id) {
    return deviceRepo.findById(id);
  }

  @Override
  public List<Device> getAll() {
    return deviceRepo.findAll();
  }

  @Scheduled(
    timeUnit = TimeUnit.MINUTES,
    fixedDelayString = "${pull.devices.metadata.fixed.delay:10}",
    initialDelayString = "${pull.devices.metadata.initial.delay:1}")
  @Override
  public void pullDeviceMetadata() {

    log.info("Devices metadata update started");

    for (Device device : deviceRepo.findAll()) {

      DeviceInfo deviceInfo = device.getDeviceInfo();
      if (deviceInfo == null) {
        deviceInfo = new DeviceInfo();
        device.setDeviceInfo(deviceInfo);
        deviceInfo.setDevice(device);
      }

      try {

        FonoapiDeviceDto deviceDto = fonoapiService.pullDeviceInfo(device);
        if (deviceDto == null) {
          log.warn("New device metadata hasn't been provided for device '{} - {}'", device.getBrand(), device.getModelName());
          continue;
        }
        deviceInfo.setTechnology(deviceDto.technology());
        deviceInfo.setBands2G(deviceDto.bands2G());
        deviceInfo.setBands3G(deviceDto.bands3G());
        deviceInfo.setBands4G(deviceDto.bands4G());

        deviceInfoRepo.save(deviceInfo);

      } catch (DataAccessException e) {

        log.error("Can't persist data '{}' for device '{} - {}'", deviceInfo, device.getBrand(), device.getModelName(), e);

      } catch (RestClientException rce) {

        log.error("Can't pull data for device {} - {}", device.getBrand(), device.getModelName());
      }
    }

    log.info("Devices metadata update ended");
  }

}
