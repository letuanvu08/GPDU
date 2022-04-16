package hcmut.thesis.gpduserver.ai.utils;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

import hcmut.thesis.gpduserver.ai.models.IntegerRouting;
import hcmut.thesis.gpduserver.ai.models.Key;
import hcmut.thesis.gpduserver.mapbox.IMapboxClient;
import hcmut.thesis.gpduserver.mapbox.commands.GetDurationCommand;
import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.utils.TimeUtils;
import java.util.ArrayList;
import java.util.List;

public class RoutingOperation {

    private static float calRouteDuration(List<Location> route, IMapboxClient mapboxClient) {
        float duration = 0;
        long timestamp = System.currentTimeMillis() / 1000 + 60 * 30;
        for (int i = 0; i < route.size() - 1; i++) {
            GetDurationCommand command = GetDurationCommand.builder()
                .fromLocation(route.get(i))
                .toLocation(route.get(i + 1))
                .departAt(TimeUtils.convertTimestampToUTC(timestamp))
                .build();
            duration += mapboxClient.getDuration(command);
        }
        return duration;
    }

    public static float calTotalDuration(List<Key<IntegerRouting>> keys, List<Order> orders, IMapboxClient mapboxClient) {
        List<Location> locations = new ArrayList<>();
        List<List<Location>> routes = new ArrayList<>();
        int vehicle = 0;
        for (Key<IntegerRouting> key : keys) {
            int temp = key.getValue().getVehicle();
            if (temp != vehicle) {
                vehicle = temp;
                routes.add(locations);
                locations = new ArrayList<>();
                locations.add(getCoordinates(key, orders));
            } else {
                locations.add(getCoordinates(key, orders));
            }
        }
        float duration = 0;
        for (List<Location> route : routes) {
            duration += calRouteDuration(route, mapboxClient);
        }
        return duration;
    }

    private static Location getCoordinates(Key<IntegerRouting> key, List<Order> orders) {
        Location location;
        Order order = orders.get(key.getOrderIndex());
        if (key.getType().equals(PICKUP.name())) {
            location = order.getPickup().getLocation();
        } else {
            location = order.getDelivery().getLocation();
        }
        return location;
    }

}
