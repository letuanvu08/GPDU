package hcmut.thesis.gpduserver.mapbox.commands;


import hcmut.thesis.gpduserver.models.entity.Location;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetDurationCommand {
    private Location fromLocation;
    private Location toLocation;
    private String departAt;
}
