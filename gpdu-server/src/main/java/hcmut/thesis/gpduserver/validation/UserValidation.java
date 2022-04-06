package hcmut.thesis.gpduserver.validation;

import hcmut.thesis.gpduserver.constants.enumations.BaseCodeEnum;
import hcmut.thesis.gpduserver.constants.enumations.TypeUser;
import hcmut.thesis.gpduserver.models.Exception.ValidationException;
import hcmut.thesis.gpduserver.models.request.user.FormRegister;
import hcmut.thesis.gpduserver.models.request.user.LoginRequest;
import hcmut.thesis.gpduserver.service.UserService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidation {
  @Autowired
  private UserService userService;

  public void asserRegisterAccount(FormRegister formRegister) throws ValidationException {
    if(formRegister.getUserName().isBlank() || formRegister.getEmail().isBlank() || formRegister.getPassword().isBlank()){
      throw new ValidationException(BaseCodeEnum.INVALID_PARAMS);
    }
    if(Objects.nonNull(userService.getUserByName(formRegister.getUserName()))){
      throw new ValidationException(BaseCodeEnum.USER_NAME_PASSWORD_INVALID);
    }
    if(formRegister.getType().equals(TypeUser.UNKNOW)){
      throw new ValidationException(BaseCodeEnum.INVALID_PARAMS);
    }
  }
  public void assertLogin(LoginRequest request) throws ValidationException {
    if(request.getUserName().isBlank() || request.getPassword().isBlank()){
      throw new ValidationException(BaseCodeEnum.INVALID_PARAMS);
    }
  }
}
