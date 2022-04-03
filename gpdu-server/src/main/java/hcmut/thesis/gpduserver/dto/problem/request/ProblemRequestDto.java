package hcmut.thesis.gpduserver.dto.problem.request;

import hcmut.thesis.gpduserver.dto.problem.Node;
import hcmut.thesis.gpduserver.dto.problem.PickupDelivery;
import hcmut.thesis.gpduserver.dto.problem.Vehicle;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProblemRequestDto {
  private Integer numberVehicles;
  private Integer numberNode;
  private List<Vehicle> vehicles;
  private List<PickupDelivery> pickupDeliveries;
}
