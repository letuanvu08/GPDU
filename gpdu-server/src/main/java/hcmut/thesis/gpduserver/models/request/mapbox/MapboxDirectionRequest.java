package hcmut.thesis.gpduserver.models.request.mapbox;

import hcmut.thesis.gpduserver.models.entity.Location;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapboxDirectionRequest {
  private List<Location> locations;

}
