package hcmut.thesis.gpduserver.dto.problem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PickupDelivery {
  private Node pickup;
  private Node delivery;
  private double weight;
  private double volume;
}
