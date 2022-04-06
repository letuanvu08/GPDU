package hcmut.thesis.gpduserver.models.Exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

  private int code;
  private String message;

  public ApiException(int code,String message) {
    super(String.format("Code %s - Error %s",code,message));
    this.code = code;
    this.message = message;
  }
}
