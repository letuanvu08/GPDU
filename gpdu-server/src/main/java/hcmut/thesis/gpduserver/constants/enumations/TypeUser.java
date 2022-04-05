package hcmut.thesis.gpduserver.constants.enumations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public enum TypeUser {
  @JsonProperty("CUSTOMER")
  CUSTOMER("CUSTOMER"),
  @JsonProperty("DRIVER")
  DRIVER("DRIVER"),
  @JsonProperty("ADMIN")
  ADMIN("ADMIN"),
  UNKNOW("UNKNOW");

  private String type;

  private static Map<String, TypeUser> parse = new HashMap<>();


  TypeUser(String type) {
    this.type = type;
  }

  public static TypeUser fromCode(String type) {
    if (parse.isEmpty()) {
      for (TypeUser  typeUser : values()) {
        parse.put(typeUser.type, typeUser);
      }
    }
    return parse.getOrDefault(type, TypeUser.UNKNOW);
  }

}
