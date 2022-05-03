package hcmut.thesis.gpduserver.ai.testcase;


import hcmut.thesis.gpduserver.ai.models.RoutingOrder;
import hcmut.thesis.gpduserver.models.entity.Location;

import java.util.List;

public class TestCaseConverter {
    public static RoutingOrder convertString2RoutingOrder(String input) {
        String[] objectList = input.split("[ ]+");
        return RoutingOrder.builder()
                .weight(Long.parseLong(objectList[1]))
                .pickup(RoutingOrder.RoutingNode.builder()
                        .location(Location.builder()
                                .latitude(Float.parseFloat(objectList[2]))
                                .longitude(Float.parseFloat(objectList[3]))
                                .build())
                        .earliestTime(System.currentTimeMillis() + Long.parseLong(objectList[4]) * 1000)
                        .latestTime(System.currentTimeMillis() + Long.parseLong(objectList[5]) * 1000)
                        .build())
                .delivery(RoutingOrder.RoutingNode.builder()
                        .location(Location.builder()
                                .latitude(Float.parseFloat(objectList[6]))
                                .longitude(Float.parseFloat(objectList[7]))
                                .build())
                        .earliestTime(System.currentTimeMillis() + Long.parseLong(objectList[8]) * 1000)
                        .latestTime(System.currentTimeMillis() + Long.parseLong(objectList[9]) + 1000)
                        .build())
                .build();
    }

    public static Location convertString2VehicleLocation(String input) {
        String[] objectList = input.split("[ ]+");
        return Location.builder()
                .latitude(Float.parseFloat(objectList[1]))
                .longitude(Float.parseFloat(objectList[2]))
                .build();
    }

    public static Location convertString2RepoLocation(String input) {
        String[] objectList = input.split("\t")[1].split(",");
        return Location.builder()
                .latitude(Float.parseFloat(objectList[0]))
                .longitude(Float.parseFloat(objectList[1]))
                .build();
    }
}
