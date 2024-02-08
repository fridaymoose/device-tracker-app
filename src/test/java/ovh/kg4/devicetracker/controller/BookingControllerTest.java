package ovh.kg4.devicetracker.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ovh.kg4.devicetracker.dto.BookingDto;
import ovh.kg4.devicetracker.entity.Booking;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.entity.DeviceInfo;
import ovh.kg4.devicetracker.entity.User;
import ovh.kg4.devicetracker.mapstruct.BookingMapper;
import ovh.kg4.devicetracker.service.BookingService;
import ovh.kg4.devicetracker.service.DeviceService;
import ovh.kg4.devicetracker.service.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  BookingService bookingService;
  @MockBean
  DeviceService deviceService;
  @MockBean
  UserService userService;
  @MockBean
  BookingMapper bookingMapper;

  @ParameterizedTest
  @MethodSource("provideBooking")
  @WithMockUser(username = "test-user", password = "test-password")
  void getAll(Booking booking, BookingDto bookingDto) throws Exception {

    List<Booking> bookings = new ArrayList<>();
    bookings.add(booking);

    given(bookingService.getAll()).willReturn(bookings);
    given(bookingMapper.toDto(booking)).willReturn(bookingDto);

    mvc.perform(get("/bookings")
        .with(httpBasic("test-user", "test-password"))
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
      .andExpect(jsonPath("$[0].booked_at", is(booking.getBookedAt()), Long.class))
      .andExpect(jsonPath("$[0].booked_by", is(booking.getBookedBy().getName())))
      .andExpect(jsonPath("$[0].userId", is(booking.getBookedBy().getId()), Long.class))
      .andExpect(jsonPath("$[0].deviceName", is(booking.getDevice().getModelName())))
      .andExpect(jsonPath("$[0].deviceId", is(booking.getDevice().getId()), Long.class))
    ;
  }

  @Test
  void getByDevice() {
  }

  @Test
  void bookDevice() {
  }

  @Test
  void releaseDevice() {
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

    BookingDto bookingDto = new BookingDto(booking01.getId(), booking01.getDevice().getId(),
      booking01.getDevice().getModelName(), booking01.getBookedBy().getId(), booking01.getBookedBy().getName(),
      booking01.getBookedAt());

    return Stream.of(
      Arguments.of(booking01, bookingDto)
    );
  }
}
