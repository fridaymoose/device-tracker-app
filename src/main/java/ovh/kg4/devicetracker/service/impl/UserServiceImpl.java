package ovh.kg4.devicetracker.service.impl;

import org.springframework.stereotype.Service;
import ovh.kg4.devicetracker.entity.User;
import ovh.kg4.devicetracker.repository.UserRepo;
import ovh.kg4.devicetracker.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepo userRepo;

  public UserServiceImpl(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public Optional<User> getId(Long id) {

    return userRepo.findById(id);
  }
}
