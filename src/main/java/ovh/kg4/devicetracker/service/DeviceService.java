package ovh.kg4.devicetracker.service;

import ovh.kg4.devicetracker.entity.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

  Optional<Device> getId(Long id);

  List<Device> getAll();

  void pullDeviceMetadata();

}
