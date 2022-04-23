package hcmut.thesis.gpduserver.models.request.routing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestGetRouting {
    private String orderId;
    private String vehicleId;
}
