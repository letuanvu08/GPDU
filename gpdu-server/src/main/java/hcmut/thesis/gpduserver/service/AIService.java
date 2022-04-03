package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.dto.problem.reponse.ProblemResponseDto;
import hcmut.thesis.gpduserver.dto.problem.request.ProblemRequestDto;

public interface AIService {
  ProblemResponseDto submitProblem(ProblemRequestDto problemRequestDto);
}
