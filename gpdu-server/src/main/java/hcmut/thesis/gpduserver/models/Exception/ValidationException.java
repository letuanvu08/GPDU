package hcmut.thesis.gpduserver.models.Exception;

import hcmut.thesis.gpduserver.constants.enumations.BaseCodeEnum;
import java.io.IOException;
import lombok.Getter;

@Getter
public class ValidationException extends IOException {

  private static final long serialVersionUID = -4822531898545233653L;

  private final BaseCodeEnum baseCodeEnum;
  public ValidationException(BaseCodeEnum baseCodeEnum) {
    super(baseCodeEnum.getMessage());
    this.baseCodeEnum = baseCodeEnum;

  }
}
