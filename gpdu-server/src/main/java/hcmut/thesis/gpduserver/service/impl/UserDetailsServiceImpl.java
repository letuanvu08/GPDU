package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.models.entity.User;
import hcmut.thesis.gpduserver.models.entity.UserSecure;
import hcmut.thesis.gpduserver.service.UserService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserService userService;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.getUserByName(username);
    if(Objects.isNull(user)){
      return null;
    }
    return new UserSecure(user);
  }
}
