package ovh.kg4.devicetracker.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ovh.kg4.devicetracker.entity.Booking;
import ovh.kg4.devicetracker.entity.Device;

@Repository
public interface BookingRepo extends PagingAndSortingRepository<Booking, Long>, ListCrudRepository<Booking, Long> {

  @Transactional
  int deleteBookingByDevice(Device device);
}
