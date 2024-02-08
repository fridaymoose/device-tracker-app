package ovh.kg4.devicetracker.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ovh.kg4.devicetracker.entity.User;
import ovh.kg4.devicetracker.repository.UserRepo;
import ovh.kg4.devicetracker.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

  @Mock
  UserRepo repo;

  UserService userService;

  AutoCloseable closeable;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
    userService = new UserServiceImpl(repo);
  }

  @Test
  void getId() {

    Long userId = 1_000L;

    User user = new User();
    user.setId(userId);
    user.setName("Pavel Kravchenko");
    user.setEmail("pavel.kravchenko@kg4.ovh");

    Optional<User> result = Optional.of(user);

    when(repo.findById(userId)).thenReturn(result);

    assertEquals(result, userService.getId(userId));
    verify(repo, times(1)).findById(userId);
  }
}