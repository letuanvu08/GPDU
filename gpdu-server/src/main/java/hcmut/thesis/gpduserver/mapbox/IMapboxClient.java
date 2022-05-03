package hcmut.thesis.gpduserver.mapbox;

import hcmut.thesis.gpduserver.mapbox.commands.GetDurationCommand;
import hcmut.thesis.gpduserver.models.entity.Location;

import java.util.List;
import java.util.Optional;

public interface IMapboxClient {
    Float getDuration(GetDurationCommand command);

    Optional<List<List<Float>>> retrieveDurationMatrix(List<Location> locations);

    Optional<List<List<Float>>> retrieveDurationMatrix(List<Location> src, List<Location> des);

    String reverseGeocoding(Location location);

}
