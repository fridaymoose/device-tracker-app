package ovh.kg4.devicetracker.service.impl;

import org.springframework.stereotype.Service;
import ovh.kg4.devicetracker.entity.Booking;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.repository.BookingRepo;
import ovh.kg4.devicetracker.service.BookingService;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

  private final BookingRepo bookingRepo;

  public BookingServiceImpl(BookingRepo bookingRepo) {

    this.bookingRepo = bookingRepo;
  }

  @Override
  public List<Booking> getAll() {
    return bookingRepo.findAll();
  }

  @Override
  public Optional<Booking> getByDevice(Long id) {

    return bookingRepo.findById(id);
  }

  @Override
  public Booking save(Booking booking) {

    return bookingRepo.save(booking);
  }

  @Override
  public int deleteByDevice(Device device) {
    return bookingRepo.deleteBookingByDevice(device);
  }
}
