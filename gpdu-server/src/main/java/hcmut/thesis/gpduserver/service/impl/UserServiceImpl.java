package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.models.entity.User;
import hcmut.thesis.gpduserver.models.reponse.user.PairToken;
import hcmut.thesis.gpduserver.models.request.user.FormRegister;
import hcmut.thesis.gpduserver.models.request.user.LoginRequest;
import hcmut.thesis.gpduserver.repository.UserRepository;
import hcmut.thesis.gpduserver.utils.JwttUtil;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements hcmut.thesis.gpduserver.service.UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwttUtil jwttUtil;


  @Override
  public User registerUser(FormRegister formRegister) {
    try {

      Optional<User> userResult = this.userRepository.insert(User.builder()
          .userName(formRegister.getUserName())
          .email(formRegister.getEmail())
          .password(formRegister.getPassword())
          .typeUser(formRegister.getType().name())
          .build());
      log.info("registerUser form: {}, result: {}", formRegister, userResult.orElse(null));
      return userResult.orElse(null);
    } catch (Exception e) {
      log.error("Error when register user : {}", e.getMessage());
      return null;
    }
  }

  @Override
  public User getUserByName(String userName) {
    try {
      Document request = new Document().append("userName", userName);
      Optional<User> userOptional = this.userRepository.getByQuery(request);
      log.info("getUserByName userName: {}, result: {}", userName, userOptional.orElse(null));
      return userOptional.orElse(null);
    } catch (Exception e) {
      log.error("Error when GetUserByName: {}", e.getMessage());
      return null;
    }
  }

  @Override
  public User getUserById(String id) {
    try {
      Optional<User> userOptional = this.userRepository.getById(id);
      log.info("getUserById getUserById: {}, result: {}", id, userOptional.orElse(null));
      return userOptional.orElse(null);
    } catch (Exception e) {
      log.error("Error when GetUserById: {}", e.getMessage());
      return null;
    }
  }

  @Override
  public PairToken getTokenByUserName(LoginRequest loginrequest) {
    try {
      Document request = new Document().append("userName", loginrequest.getUserName())
          .append("password", loginrequest.getPassword());
      Optional<User> userOptional = this.userRepository.getByQuery(request);
      AtomicReference<PairToken> pairToken = new AtomicReference<>();
      userOptional.ifPresent(user -> {
        pairToken.set(PairToken.builder()
            .token(jwttUtil.generateToken(user))
            .refreshToken(jwttUtil.generateRefreshToken(user))
            .build());
      });
      return pairToken.get();
    } catch (Exception e) {
      log.error("Error when getTokenByUserName: {}", e.getMessage());
      return null;
    }
  }

}
