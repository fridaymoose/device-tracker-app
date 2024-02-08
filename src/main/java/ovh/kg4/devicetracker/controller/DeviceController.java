package ovh.kg4.devicetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.kg4.devicetracker.dto.DeviceDto;
import ovh.kg4.devicetracker.dto.DeviceInfoDto;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.exception.ValidationException;
import ovh.kg4.devicetracker.mapstruct.DeviceMapper;
import ovh.kg4.devicetracker.service.DeviceService;

import java.util.List;

@RestController
@RequestMapping("devices")
public class DeviceController {
  private final DeviceService deviceService;
  private final DeviceMapper deviceMapper;

  public DeviceController(DeviceService deviceService, DeviceMapper deviceMapper) {

    this.deviceService = deviceService;
    this.deviceMapper = deviceMapper;
  }

  @GetMapping
  List<DeviceInfoDto> getAll() {

    List<Device> devices = deviceService.getAll();

    return devices.stream()
      .map(deviceMapper::toDeviceInfoDto)
      .toList();
  }

  @GetMapping("/status")
  List<DeviceDto> getAllWithBookingInfo() {

    List<Device> devices = deviceService.getAll();

    return devices.stream()
      .map(deviceMapper::toDto)
      .toList();
  }

  @GetMapping("{id}")
  DeviceInfoDto getById(@PathVariable Long id) {

    Device device = deviceService.getId(id)
      .orElseThrow(() -> new ValidationException("Device not found"));

    return deviceMapper.toDeviceInfoDto(device);
  }

}