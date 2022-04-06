package hcmut.thesis.gpduserver.models.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListRequest {

  private String userId;
  private String vehicleId;
  private String labelStep;
  private String status;
  @Value("0")
  private int offset;
  @Value("100")
  private int limit;
}
