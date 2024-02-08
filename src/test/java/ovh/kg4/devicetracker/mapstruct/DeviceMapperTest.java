package ovh.kg4.devicetracker.mapstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ovh.kg4.devicetracker.dto.DeviceDto;
import ovh.kg4.devicetracker.dto.DeviceInfoDto;
import ovh.kg4.devicetracker.entity.Booking;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.entity.DeviceInfo;
import ovh.kg4.devicetracker.entity.User;

import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DeviceMapperTest {

  DeviceMapper deviceMapper;

  @BeforeEach
  void setUp() {

    deviceMapper = new DeviceMapperImpl();
  }

  @ParameterizedTest
  @MethodSource("provideDevices")
  void toDto(Device device) {

    DeviceDto dto = deviceMapper.toDto(device);

    assertEquals(device.getId(), dto.id());
    assertEquals(device.getModelName(), dto.modelName());
    assertEquals(device.getBooking() == null, dto.available());

    if (device.getBooking() == null) {
      assertNull(dto.userId());
      assertNull(dto.userName());
      assertNull(dto.bookedAt());
    } else {
      assertEquals(device.getBooking().getBookedBy().getId(), dto.userId());
      assertEquals(device.getBooking().getBookedBy().getName(), dto.userName());
      assertEquals(device.getBooking().getBookedAt(), dto.bookedAt());
    }

    if (device.getDeviceInfo() == null) {
      assertNull(dto.technology());
      assertNull(dto.bands2G());
      assertNull(dto.bands3G());
      assertNull(dto.bands4G());
    } else {
      assertEquals(device.getDeviceInfo().getTechnology(), dto.technology());
      assertEquals(device.getDeviceInfo().getBands2G(), dto.bands2G());
      assertEquals(device.getDeviceInfo().getBands3G(), dto.bands3G());
      assertEquals(device.getDeviceInfo().getBands4G(), dto.bands4G());
    }
  }

  @ParameterizedTest
  @MethodSource("provideDevices")
  void toDeviceInfoDto(Device device) {

    DeviceInfoDto dto = deviceMapper.toDeviceInfoDto(device);

    assertEquals(device.getId(), dto.id());
    assertEquals(device.getModelName(), dto.modelName());

    if (device.getDeviceInfo() == null) {
      assertNull(dto.technology());
      assertNull(dto.bands2G());
      assertNull(dto.bands3G());
      assertNull(dto.bands4G());
    } else {
      assertEquals(device.getDeviceInfo().getTechnology(), dto.technology());
      assertEquals(device.getDeviceInfo().getBands2G(), dto.bands2G());
      assertEquals(device.getDeviceInfo().getBands3G(), dto.bands3G());
      assertEquals(device.getDeviceInfo().getBands4G(), dto.bands4G());
    }
  }

  static Stream<Arguments> provideDevices() {

    // device #1
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

    // device #2
    Device nokia3310 = new Device();
    nokia3310.setId(2L);
    nokia3310.setBrand("Nokia");
    nokia3310.setModelName("Nokia 3310");

    // device #3
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
      Arguments.of(galaxyS9),
      Arguments.of(nokia3310),
      Arguments.of(galaxyS8)
    );
  }
}
