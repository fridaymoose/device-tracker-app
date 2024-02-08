package ovh.kg4.devicetracker.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ovh.kg4.devicetracker.entity.Booking;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.entity.User;

import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookingRepoTest {

  @Autowired
  BookingRepo bookingRepo;

  @Autowired
  TestEntityManager entityManager;

  @Test
  void testRepetitiveBooking() {

    Device device = entityManager.find(Device.class, 2L);
    User user = entityManager.find(User.class, 1L);

    Booking booking01 = new Booking();
    booking01.setDevice(device);
    booking01.setBookedBy(user);
    booking01.setBookedAt(Instant.now().getEpochSecond());
    device.setBooking(booking01);

    User user02 = entityManager.find(User.class, 2L);
    Booking booking02 = new Booking();
    booking02.setDevice(device);
    booking02.setBookedBy(user02);
    booking02.setBookedAt(Instant.now().getEpochSecond());
    device.setBooking(booking02);

    entityManager.persist(booking01);
    Exception exception = assertThrows(ConstraintViolationException.class,
      () -> entityManager.persist(booking02)
    );

    assertTrue(exception.getMessage().contains("Unique index or primary key violation"));
  }

  @ParameterizedTest
  @MethodSource("provideBooking")
  void deleteBookingByDevice(Long userId, Long deviceId) {

    Device device = entityManager.find(Device.class, deviceId);
    User user = entityManager.find(User.class, userId);

    Booking booking01 = new Booking();
    booking01.setDevice(device);
    booking01.setBookedBy(user);
    booking01.setBookedAt(Instant.now().getEpochSecond());
    device.setBooking(booking01);

    entityManager.persist(booking01);

    assertEquals(1, bookingRepo.deleteBookingByDevice(device));

    assertNull(entityManager.find(Booking.class, booking01.getId()));
  }

  static Stream<Arguments> provideBooking() {

    return Stream.of(
      Arguments.of(1L, 1L),
      Arguments.of(2L, 2L),
      Arguments.of(1L, 3L)
    );
  }
}