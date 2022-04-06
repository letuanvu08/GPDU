package hcmut.thesis.gpduserver.constants.enumations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseCodeEnum {
  SUCCESS(1, "SUCCESS"),
  FAIL(-1, "FAIL"),
  DATA_ERROR(-2, "DATA ERROR / RESPONSE IS EMPTY"),
  SYSTEM_ERROR(-3, "SYSTEM_ERROR"),
  INVALID_PARAMS(-5, "INVALID_PARAMS"),
  UNAUTHENTICATED(-6, "UNAUTHENTICATED"),
  UNAUTHORIZED(-7, "UNAUTHORIZED"),
  USER_NAME_PASSWORD_INVALID(-8, "USER_NAME_PASSWORD_INVALID");

  private int code;
  private String message;
}
