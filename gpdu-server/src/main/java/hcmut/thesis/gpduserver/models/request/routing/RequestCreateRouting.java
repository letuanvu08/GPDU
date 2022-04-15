package hcmut.thesis.gpduserver.models.request.routing;

import hcmut.thesis.gpduserver.models.entity.Node;
import hcmut.thesis.gpduserver.models.entity.Routing.NodeRouting;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateRouting {
  private String vehicleId;
  private List<String> listOrderId;
  private List<NodeRouting> nodes;
}
