package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.entity.User;
import hcmut.thesis.gpduserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements hcmut.thesis.gpduserver.service.UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User RegisterUser(User user) {
    try {
      if (Objects.nonNull(this.GetUserByName(user.getUserName()))) {
        return null;
      }
      Optional<User> userResult = this.userRepository.insert(user);
      return userResult.orElse(null);
    } catch (Exception e) {
      log.error("Error when register user : {}", e);
      return null;
    }
  }

  @Override
  public User GetUserByName(String userName) {
    try {
      Document request = new Document().append("userName", userName);
      Optional<User> userOptional = this.userRepository.getByQuery(request);
      return userOptional.orElse(null);
    } catch (Exception e) {
      log.error("Error when GetUserByName: {}", e);
      return null;
    }
  }

  @Override
  public User GetUserById(String id) {
    try {
      Optional<User> userOptional = this.userRepository.getById(id);
      return userOptional.orElse(null);
    } catch (Exception e) {
      log.error("Error when GetUserById: {}", e);
      return null;
    }

  }

}
