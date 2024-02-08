package ovh.kg4.devicetracker.service;

import ovh.kg4.devicetracker.entity.Booking;

import java.util.List;
import java.util.Optional;
import ovh.kg4.devicetracker.entity.Device;

public interface BookingService {

  Optional<Booking> getByDevice(Long id);

  List<Booking> getAll();

  Booking save(Booking booking);

  int deleteByDevice(Device device);
}
