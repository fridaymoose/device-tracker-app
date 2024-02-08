package ovh.kg4.devicetracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingDto(
  Long id,
  Long deviceId,
  String deviceName,
  Long userId,
  @JsonProperty("booked_by")
  String userName,
  @JsonProperty("booked_at")
  Long bookedAt
) {
}
