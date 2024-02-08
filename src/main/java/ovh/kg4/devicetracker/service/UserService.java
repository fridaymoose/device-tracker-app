package ovh.kg4.devicetracker.service;

import ovh.kg4.devicetracker.entity.User;

import java.util.Optional;

public interface UserService {

  Optional<User> getId(Long id);
}
