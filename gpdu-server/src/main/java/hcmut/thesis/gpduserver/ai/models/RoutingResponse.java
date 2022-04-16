package hcmut.thesis.gpduserver.ai.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoutingResponse {
    private List<Route> routes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Route {
        private Integer vehicleId;
        private List<RoutingKey> routingKeys;
    }

}
