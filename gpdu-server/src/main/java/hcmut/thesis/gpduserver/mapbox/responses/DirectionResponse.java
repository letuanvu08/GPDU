package hcmut.thesis.gpduserver.mapbox.responses;

import lombok.Data;

import java.util.List;

@Data
public class DirectionResponse {
    private String code;
    private List<Route> routes;

    @Data
    public static class Route {
        private Float duration;
        private Float distance;
    }
}
