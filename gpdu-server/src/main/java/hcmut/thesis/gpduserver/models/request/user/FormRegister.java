package hcmut.thesis.gpduserver.models.request.user;

import hcmut.thesis.gpduserver.constants.enumations.TypeUser;
import lombok.Data;

@Data
public class FormRegister {
  private String userName;
  private String password;
  private String email;
  private TypeUser type;
}
