package hcmut.thesis.gpduserver.mapbox;

import hcmut.thesis.gpduserver.mapbox.commands.GetDurationCommand;

public interface IMapboxClient {
    Float getDuration(GetDurationCommand command);
}
