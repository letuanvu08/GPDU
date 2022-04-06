package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.models.entity.User;
import hcmut.thesis.gpduserver.models.reponse.user.PairToken;
import hcmut.thesis.gpduserver.models.request.user.FormRegister;
import hcmut.thesis.gpduserver.models.request.user.LoginRequest;

public interface UserService {
  User registerUser(FormRegister formRegister);

  User getUserByName(String userName);

  User getUserById(String  id);

  PairToken getTokenByUserNamePassword(LoginRequest request);

  Boolean assignVehicleForUser(String vehicleId, String userId);

  Boolean updateUser(User user);


}
