package hcmut.thesis.gpduserver.authentication;

import hcmut.thesis.gpduserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceIml implements UserDetailsService {
  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    hcmut.thesis.gpduserver.entity.User user = userService.GetUserByName(username);
    return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
  }
}
