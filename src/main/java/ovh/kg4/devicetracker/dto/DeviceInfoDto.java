package ovh.kg4.devicetracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "modelName"})
public record DeviceInfoDto(
  Long id,
  @JsonProperty("model")
  String modelName,
  String technology,
  @JsonProperty("2g_bands")
  String bands2G,
  @JsonProperty("3g_bands")
  String bands3G,
  @JsonProperty("4g_bands")
  String bands4G
) {
}
