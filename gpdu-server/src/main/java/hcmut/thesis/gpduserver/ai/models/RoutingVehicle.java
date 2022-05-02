package hcmut.thesis.gpduserver.ai.models;

import hcmut.thesis.gpduserver.models.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutingVehicle {
    private Integer id;
    private Location location;
    private RoutingKey nextNode;
    private Long load;
    private Long volume;
}
