package hcmut.thesis.gpduserver.models.reponse.user;

import hcmut.thesis.gpduserver.models.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
  private String id;
  private String userName;
  private String email;
  private String typeUser;

  public static UserDto convertUserDto(User user){
    return UserDto.builder()
        .email(user.getEmail())
        .id(user.getId().toString())
        .typeUser(user.getTypeUser())
        .userName(user.getUserName())
        .build();
  }
}
