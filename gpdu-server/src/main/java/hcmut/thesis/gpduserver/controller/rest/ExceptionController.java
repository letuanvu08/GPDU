package hcmut.thesis.gpduserver.controller.rest;

import hcmut.thesis.gpduserver.constants.enumations.BaseCodeEnum;
import hcmut.thesis.gpduserver.models.Exception.ValidationException;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j(topic = "#Exception Controller")
public class ExceptionController {

  @ExceptionHandler(value = {ValidationException.class})
  public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
      ValidationException ex) {
    ApiResponse<Object> failResp = new ApiResponse<>();
    log.info("Validation failed output {}",ex.getBaseCodeEnum().getMessage());
    failResp.fail(ex.getBaseCodeEnum());
    return new ResponseEntity<>(failResp, HttpStatus.OK);
  }

  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<ApiResponse<Object>> handleOthersExceptions(
      Exception ex) {
    ApiResponse<Object> failResp = new ApiResponse<>();
    log.error("Internal error: {}", ex);
    failResp.fail(BaseCodeEnum.SYSTEM_ERROR);
    failResp.setData(ex.getMessage());
    return new ResponseEntity<>(failResp, HttpStatus.OK);
  }
}
