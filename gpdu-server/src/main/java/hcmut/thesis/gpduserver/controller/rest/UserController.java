package hcmut.thesis.gpduserver.controller.rest;

import hcmut.thesis.gpduserver.constants.enumations.BaseCodeEnum;
import hcmut.thesis.gpduserver.models.Exception.ValidationException;
import hcmut.thesis.gpduserver.models.entity.User;
import hcmut.thesis.gpduserver.models.entity.UserSecure;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import hcmut.thesis.gpduserver.models.reponse.user.PairToken;
import hcmut.thesis.gpduserver.models.reponse.user.UserDto;
import hcmut.thesis.gpduserver.models.request.user.FormRegister;
import hcmut.thesis.gpduserver.models.request.user.LoginRequest;
import hcmut.thesis.gpduserver.service.UserService;
import hcmut.thesis.gpduserver.validation.UserValidation;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserValidation userValidation;

  @PostMapping("/register")
  public ApiResponse RegisterAccount(@RequestBody  FormRegister formRegister) throws ValidationException {
    userValidation.asserRegisterAccount(formRegister);
    User user = userService.registerUser(formRegister);
    if (Objects.isNull(user)) {
      return new ApiResponse<>().fail(BaseCodeEnum.FAIL);
    }
    UserDto userDto = UserDto.convertUserDto(user);
    return new ApiResponse<>().success(userDto);
  }

  @PostMapping("/login")
  public ApiResponse login(@RequestBody LoginRequest request) throws ValidationException {
    userValidation.assertLogin(request);
    PairToken token = userService.getTokenByUserName(request);
    if (Objects.isNull(token)) {
      return new ApiResponse().fail(BaseCodeEnum.USER_NAME_PASSWORD_INVALID);
    }
    return new ApiResponse<>().success(token);
  }

  @GetMapping("/user")
  public ApiResponse getUserProfile(Authentication authentication) throws ValidationException {

    UserSecure userSecure = (UserSecure) authentication.getPrincipal();
    User user = userService.getUserById(userSecure.getId());
    if (Objects.isNull(user)) {
      return new ApiResponse().fail(BaseCodeEnum.FAIL);
    }
    return new ApiResponse<>().success(user);
  }
}
