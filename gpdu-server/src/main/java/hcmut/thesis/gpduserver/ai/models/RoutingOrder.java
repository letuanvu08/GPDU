package hcmut.thesis.gpduserver.ai.models;

import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Node;
import hcmut.thesis.gpduserver.models.entity.Routing;
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
    private RoutingNode pickup;
    private RoutingNode delivery;
    private Integer vehicleId;
    private Boolean vehicleConstant;
    private Long weight;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutingNode implements Comparable<RoutingNode> {
        private Location location;
        private Long earliestTime;
        private Long latestTime;

        @Override
        public int compareTo(RoutingNode other) {
            return (int) (earliestTime - other.getEarliestTime());
        }
    }
}
