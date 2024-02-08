package ovh.kg4.devicetracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.kg4.devicetracker.dto.BookingDto;
import ovh.kg4.devicetracker.entity.Booking;
import ovh.kg4.devicetracker.entity.Device;
import ovh.kg4.devicetracker.entity.User;
import ovh.kg4.devicetracker.exception.ValidationException;
import ovh.kg4.devicetracker.mapstruct.BookingMapper;
import ovh.kg4.devicetracker.service.BookingService;
import ovh.kg4.devicetracker.service.DeviceService;
import ovh.kg4.devicetracker.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("bookings")
@Slf4j
public class BookingController {

  private final BookingService bookingService;

  private final DeviceService deviceService;

  private final UserService userService;

  private final BookingMapper bookingMapper;

  public BookingController(
    BookingService bookingService, DeviceService deviceService,
    UserService userService, BookingMapper bookingMapper) {

    this.bookingService = bookingService;
    this.deviceService = deviceService;
    this.userService = userService;
    this.bookingMapper = bookingMapper;
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  List<BookingDto> getAll() {

    return bookingService.getAll().stream()
      .map(bookingMapper::toDto)
      .toList();
  }

  @GetMapping(value = "device/{deviceId}", produces = APPLICATION_JSON_VALUE)
  ResponseEntity<BookingDto> getByDevice(@PathVariable Long deviceId) {

    Device device = deviceService.getId(deviceId)
      .orElseThrow(() -> new ValidationException("Device not found"));

    Optional<Booking> booking = bookingService.getByDevice(device.getId());
    if (booking.isEmpty()) {
      log.debug("There is no booking for deviceId={}", deviceId);
      return ResponseEntity.status(NOT_FOUND).build();
    }

    BookingDto bookingDto = bookingMapper.toDto(booking.get());
    return ResponseEntity.ok(bookingDto);
  }

  @PostMapping(value = "device/{deviceId}/user/{userId}", produces = APPLICATION_JSON_VALUE)
  ResponseEntity<BookingDto> bookDevice(@PathVariable Long deviceId, @PathVariable Long userId) {

    Device device = deviceService.getId(deviceId)
      .orElseThrow(() -> new ValidationException("Device not found"));

    User user = userService.getId(userId)
      .orElseThrow(() -> new ValidationException("User not found"));

    Booking booking = new Booking();
    booking.setDevice(device);
    booking.setBookedBy(user);
    booking.setBookedAt(Instant.now().getEpochSecond());

    HttpStatus httpStatus = HttpStatus.OK;
    try {

      booking = bookingService.save(booking);

    } catch (DataIntegrityViolationException e) {

      log.error("Booking for devideId={} already exists!", deviceId, e);
      httpStatus = CONFLICT;
    }

    BookingDto bookingDto = bookingMapper.toDto(booking);
    return ResponseEntity.status(httpStatus).body(bookingDto);
  }

  @DeleteMapping("device/{deviceId}")
  ResponseEntity<Void> releaseDevice(@PathVariable Long deviceId) {

    Device device = deviceService.getId(deviceId)
      .orElseThrow(() -> new ValidationException("Device not found"));

    int rowsDeleted = bookingService.deleteByDevice(device);
    return ResponseEntity.status(rowsDeleted > 0 ? NO_CONTENT : NOT_FOUND).build();
  }
}
