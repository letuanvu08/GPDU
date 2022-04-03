package hcmut.thesis.gpduserver.dto.problem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vehicle {
  private double lat;
  private double lgn;
  private int id;
}
