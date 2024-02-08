package ovh.kg4.devicetracker.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ovh.kg4.devicetracker.dto.DeviceDto;
import ovh.kg4.devicetracker.dto.DeviceInfoDto;
import ovh.kg4.devicetracker.entity.Device;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeviceMapper {

  @Mapping(target = ".", source = "deviceInfo")
  @Mapping(target = "id", source = "id")
  @Mapping(target = "userId", source = "booking.bookedBy.id")
  @Mapping(target = "userName", source = "booking.bookedBy.name")
  @Mapping(target = "bookedAt", source = "booking.bookedAt")
  @Mapping(target = "available", expression = "java(entity.getBooking()==null?true:false)")
  DeviceDto toDto(Device entity);

  @Mapping(target = ".", source = "deviceInfo")
  @Mapping(target = "id", source = "id")
  DeviceInfoDto toDeviceInfoDto(Device entity);
}
