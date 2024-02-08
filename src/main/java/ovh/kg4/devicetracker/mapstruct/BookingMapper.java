package ovh.kg4.devicetracker.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ovh.kg4.devicetracker.dto.BookingDto;
import ovh.kg4.devicetracker.entity.Booking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

  @Mapping(target = "deviceId", source = "device.id")
  @Mapping(target = "deviceName", source = "device.modelName")
  @Mapping(target = "userId", source = "bookedBy.id")
  @Mapping(target = "userName", source = "bookedBy.name")
  BookingDto toDto(Booking entity);
}
