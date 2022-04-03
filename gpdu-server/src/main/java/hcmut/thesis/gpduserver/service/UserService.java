package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.entity.User;

public interface UserService {
  User RegisterUser(User user);

  User GetUserByName(String userName);

  User GetUserById(String  id);

}
