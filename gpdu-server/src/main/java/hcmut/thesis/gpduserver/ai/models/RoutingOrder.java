package hcmut.thesis.gpduserver.ai.models;

import hcmut.thesis.gpduserver.models.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoutingOrder {
    private Integer id;
    private Location pickup;
    private Location delivery;
    private Integer vehicleId;
}
