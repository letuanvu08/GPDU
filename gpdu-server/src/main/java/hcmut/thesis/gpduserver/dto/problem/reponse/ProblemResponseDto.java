package hcmut.thesis.gpduserver.dto.problem.reponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemResponseDto {
  private int status;
  private String message;
  private String code;
}
