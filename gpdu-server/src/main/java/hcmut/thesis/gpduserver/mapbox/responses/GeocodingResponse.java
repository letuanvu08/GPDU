package hcmut.thesis.gpduserver.mapbox.responses;

import lombok.Data;

import java.util.List;

@Data
public class GeocodingResponse {
    List<Feature> features;
    @Data
    public static class Feature {
        private String place_name;
    }
}
