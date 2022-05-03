package hcmut.thesis.gpduserver.ai.utils;

import hcmut.thesis.gpduserver.ai.models.RoutingKey;
import hcmut.thesis.gpduserver.ai.models.RoutingOrder;
import hcmut.thesis.gpduserver.ai.models.RoutingVehicle;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.models.entity.Vehicle;

public class RoutingConverter {
    public static RoutingOrder convertOrder2RoutingOrder(Order order, int id) {
        return RoutingOrder.builder()
                .delivery(RoutingOrder.RoutingNode.builder()
                        .earliestTime(order.getDelivery().getEarliestTime())
                        .latestTime(order.getDelivery().getLatestTime())
                        .location(order.getDelivery().getLocation())
                        .build())
                .pickup(RoutingOrder.RoutingNode.builder()
                        .earliestTime(order.getPickup().getEarliestTime())
                        .latestTime(order.getPickup().getLatestTime())
                        .build())
                .weight(order.getPackageInfo().getWeight())
                .vehicleConstant(!order.getCurrentStep().getStep().equals(StepOrderEnum.ORDER_RECEIVED.getLabel()))
                .build();
    }
    
    public static RoutingVehicle convertVehicle2RoutingVehicle(Vehicle vehicle, RoutingKey routingKey) {
        return RoutingVehicle.builder()
                .location(vehicle.getCurrentLocation())
                .load(vehicle.getCapacity())
                .volume(vehicle.getVolume())
                .nextNode(routingKey)
                .build();
    }
}
