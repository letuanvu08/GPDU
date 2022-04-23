package hcmut.thesis.gpduserver.utils;


import hcmut.thesis.gpduserver.models.entity.Location;

import java.util.concurrent.ThreadLocalRandom;

public class LocationUtils {
    public static Location generateHCMUTLocation() {
        return Location.builder()
                .latitude((float) ThreadLocalRandom.current().nextDouble(10.713218, 10.862765))
                .longitude((float) ThreadLocalRandom.current().nextDouble(106.591395, 106.686841))
                .build();
    }
}
