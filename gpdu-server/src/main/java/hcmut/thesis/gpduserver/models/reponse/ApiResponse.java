package hcmut.thesis.gpduserver.models.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hcmut.thesis.gpduserver.constants.enumations.BaseCodeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
@Data
@NoArgsConstructor
public class ApiResponse<T> {

  protected int returnCode;
  protected String returnMessage;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  public ApiResponse<T> success(T data){
    this.returnCode = BaseCodeEnum.SUCCESS.getCode();
    this.returnMessage = BaseCodeEnum.SUCCESS.getMessage();
    this.data = data;
    return this;
  }

  public ApiResponse<T> fail(BaseCodeEnum codeEnum){
    this.returnCode = codeEnum.getCode();
    this.returnMessage = codeEnum.getMessage();
    return this;
  }
}
