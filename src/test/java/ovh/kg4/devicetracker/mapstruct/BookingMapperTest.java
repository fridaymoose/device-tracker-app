package ovh.kg4.devicetracker.mapstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ovh.kg4.devicetracker.dto.BookingDto;
import ovh.kg4.devicetracker.entity.Booking;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.entity.DeviceInfo;
import ovh.kg4.devicetracker.entity.User;

import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

  BookingMapper bookingMapper;

  @BeforeEach
  void setUp() {

    bookingMapper = new BookingMapperImpl();
  }

  @ParameterizedTest
  @MethodSource("provideBookings")
  void toDto(Booking booking) {

    BookingDto dto = bookingMapper.toDto(booking);

    assertEquals(booking.getDevice().getId(), dto.deviceId());
    assertEquals(booking.getDevice().getModelName(), dto.deviceName());

    assertEquals(booking.getBookedBy().getId(), dto.userId());
    assertEquals(booking.getBookedBy().getName(), dto.userName());

    assertEquals(booking.getBookedAt(), dto.bookedAt());
  }

  static Stream<Arguments> provideBookings() {

    User user = new User();
    user.setId(1L);
    user.setName("Pavel Kravchenko");
    user.setEmail("pavel.kravchenko@kg4.ovh");

    // booking #1
    Device galaxyS9 = new Device();
    galaxyS9.setId(1L);
    galaxyS9.setBrand("Samsung");
    galaxyS9.setModelName("Samsung Galaxy S9");

    DeviceInfo galaxyS9Info = new DeviceInfo();
    galaxyS9Info.setTechnology("GSM / CDMA / HSPA / EVDO / LTE");
    galaxyS9Info.setBands2G("GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)");
    galaxyS9Info.setBands3G("HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100 - Global, USA");
    galaxyS9Info.setBands4G("1, 2, 3, 4, 5, 7, 8, 12, 13, 17, 18, 19, 20, 25, 26, 28, 32, 38, 39, 40, 41, 66 - Global");

    galaxyS9.setDeviceInfo(galaxyS9Info);

    Booking booking01 = new Booking();
    booking01.setId(10L);
    booking01.setDevice(galaxyS9);
    booking01.setBookedBy(user);
    booking01.setBookedAt(Instant.now().getEpochSecond());

    // booking #2
    Device nokia3310 = new Device();
    nokia3310.setId(2L);
    nokia3310.setBrand("Nokia");
    nokia3310.setModelName("Nokia 3310");

    Booking booking02 = new Booking();
    booking02.setId(20L);
    booking02.setDevice(nokia3310);
    booking02.setBookedBy(user);
    booking02.setBookedAt(Instant.now().getEpochSecond());

    return Stream.of(
      Arguments.of(booking01),
      Arguments.of(booking02)
    );
  }
}