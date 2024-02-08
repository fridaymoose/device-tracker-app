package ovh.kg4.devicetracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FonoapiDeviceDto(
  @JsonProperty("DeviceName")
  String deviceName,
  @JsonProperty("Brand")
  String brand,
  String technology,
  @JsonProperty("_2g_bands")
  String bands2G,
  @JsonProperty("_3g_bands")
  String bands3G,
  @JsonProperty("_4g_bands")
  String bands4G
) {
}
