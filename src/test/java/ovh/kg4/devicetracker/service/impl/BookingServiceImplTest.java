package ovh.kg4.devicetracker.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ovh.kg4.devicetracker.entity.Booking;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.entity.DeviceInfo;
import ovh.kg4.devicetracker.entity.User;
import ovh.kg4.devicetracker.repository.BookingRepo;
import ovh.kg4.devicetracker.service.BookingService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {

  @Mock
  BookingRepo repo;

  BookingService bookingService;

  AutoCloseable closeable;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
    bookingService = new BookingServiceImpl(repo);
  }

  @ParameterizedTest
  @MethodSource("provideBooking")
  void getAll(Booking booking) {

    List<Booking> result = new ArrayList<>();
    result.add(booking);

    when(repo.findAll()).thenReturn(result);

    assertEquals(result, bookingService.getAll());
    verify(repo, times(1)).findAll();
  }

  @ParameterizedTest
  @MethodSource("provideBooking")
  void getByDevice(Booking booking, Device device) {

    Optional<Booking> result = Optional.of(booking);
    when(repo.findById(device.getId())).thenReturn(result);

    assertEquals(result, bookingService.getByDevice(device.getId()));
    verify(repo, times(1)).findById(device.getId());
  }

  @ParameterizedTest
  @MethodSource("provideBooking")
  void save(Booking booking) {

    when(repo.save(booking)).thenReturn(booking);

    assertEquals(booking, bookingService.save(booking));
    verify(repo, times(1)).save(booking);
  }

  @ParameterizedTest
  @MethodSource("provideDevice")
  void deleteByDevice(Device device) {

    Integer rows = 1;

    when(repo.deleteBookingByDevice(device)).thenReturn(rows);

    assertEquals(rows, bookingService.deleteByDevice(device));
    verify(repo, times(1)).deleteBookingByDevice(device);
  }

  static Stream<Arguments> provideDevice() {

    Device nokia3310 = new Device();
    nokia3310.setId(2L);
    nokia3310.setBrand("Nokia");
    nokia3310.setModelName("Nokia 3310");

    return Stream.of(
      Arguments.of(nokia3310)
    );
  }

  static Stream<Arguments> provideBooking() {

    Device galaxyS8 = new Device();
    galaxyS8.setId(3L);
    galaxyS8.setBrand("Samsung");
    galaxyS8.setModelName("S2x Samsung Galaxy S8");

    DeviceInfo galaxyS8Info = new DeviceInfo();
    galaxyS8Info.setTechnology("GSM / HSPA / LTE");
    galaxyS8Info.setBands2G("GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)");
    galaxyS8Info.setBands3G("HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100");
    galaxyS8Info.setBands4G("1, 2, 3, 4, 5, 7, 8, 12, 13, 17, 18, 19, 20, 25, 26, 28, 32, 66, 38, 39, 40, 41");

    galaxyS8.setDeviceInfo(galaxyS8Info);

    User user = new User();
    user.setId(1L);
    user.setName("Pavel Kravchenko");
    user.setEmail("pavel.kravchenko@kg4.ovh");

    Booking booking01 = new Booking();
    booking01.setId(10L);
    booking01.setDevice(galaxyS8);
    booking01.setBookedBy(user);
    booking01.setBookedAt(Instant.now().getEpochSecond());

    galaxyS8.setBooking(booking01);

    return Stream.of(
      Arguments.of(booking01, galaxyS8)
    );
  }
}