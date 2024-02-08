package ovh.kg4.devicetracker.service;

import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import ovh.kg4.devicetracker.dto.FonoapiDeviceDto;
import ovh.kg4.devicetracker.entity.Device;

public interface FonoapiService {

  FonoapiDeviceDto pullDeviceInfo(Device device);

  FonoapiDeviceDto  pullDeviceInfoRecover(Device device);

  FonoapiDeviceDto[] callApi(HttpEntity<MultiValueMap<String, String>> entity);
}
