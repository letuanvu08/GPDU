package hcmut.thesis.gpduserver.mapbox.commands;

import hcmut.thesis.gpduserver.models.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CallMatrixAPICommand {
    private List<Location> locations;
    private List<Integer> sources;
    private List<Integer> destinations;
}
