package hcmut.thesis.gpduserver.ai.utils;

import hcmut.thesis.gpduserver.ai.models.*;
import hcmut.thesis.gpduserver.constants.enumations.TypeNode;
import hcmut.thesis.gpduserver.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.DELIVERY;
import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

public class RoutingOperation {


    public static Durations calDurations(List<Key<IntegerRouting>> keys, RoutingMatrix routingMatrix,
                                         List<RoutingOrder> routingOrders) {
        long startTime = System.currentTimeMillis();
        int prevVehicle = -1;
        float travelDuration = 0;
        float waitingDuration = 0;
        float lateDuration = 0;
        float vehicleDuration = 0;
        for (int i = 0; i < keys.size() - 1; i++) {
            Key<IntegerRouting> currentKey = keys.get(i);
            Key<IntegerRouting> nextKey = keys.get(i + 1);
            int currentVehicle = currentKey.getValue().getVehicle();
            if (prevVehicle != currentVehicle) {
                float tempDuration = routingMatrix.getDurationVehicle(currentVehicle,
                        currentKey.getOrderIndex(), currentKey.getType());
                travelDuration += tempDuration;
                vehicleDuration = tempDuration;
                RoutingOrder.RoutingNode node = currentKey.getType().equals(PICKUP) ?
                        routingOrders.get(currentKey.getOrderIndex()).getPickup() :
                        routingOrders.get(currentKey.getOrderIndex()).getDelivery();
                if (startTime + vehicleDuration * 1000 < node.getEarliestTime()) {
                    waitingDuration += (node.getEarliestTime() - startTime) / 1000f - vehicleDuration;
                    vehicleDuration = (node.getEarliestTime() - startTime) / 1000f;
                }

                if (startTime + vehicleDuration * 1000 > node.getLatestTime()) {
                    lateDuration += vehicleDuration + (startTime - node.getLatestTime()) / 1000f;
                }
                prevVehicle = currentVehicle;
            }
            float tempDuration = routingMatrix.getDurationOrder(currentKey.getOrderIndex(),
                    currentKey.getType(), nextKey.getOrderIndex(), nextKey.getType());
            travelDuration += tempDuration;
            vehicleDuration += tempDuration;
            RoutingOrder.RoutingNode node = nextKey.getType().equals(PICKUP) ?
                    routingOrders.get(nextKey.getOrderIndex()).getPickup() :
                    routingOrders.get(nextKey.getOrderIndex()).getDelivery();
            if (startTime + vehicleDuration * 1000 < node.getEarliestTime()) {
                waitingDuration += (node.getEarliestTime() - startTime) / 1000f - vehicleDuration;
                vehicleDuration = (node.getEarliestTime() - startTime) / 1000f;
            }
            if (startTime + vehicleDuration * 1000 > node.getLatestTime()) {
                lateDuration += vehicleDuration + (startTime - node.getLatestTime()) / 1000f;
            }
        }
        travelDuration += vehicleDuration;
        return Durations.builder()
                .travel(travelDuration)
                .late(lateDuration)
                .waiting(waitingDuration)
                .build();
    }

    public static List<Key<IntegerRouting>> sortKey(List<Chromosome.Gen> gens) {
        List<Key<IntegerRouting>> keys = new ArrayList<>();
        for (int i = 0; i < gens.size(); i++) {
            Chromosome.Gen gen = gens.get(i);
            keys.add(Key.<IntegerRouting>builder()
                    .value(IntegerRouting.builder()
                            .randomKey(gen.getPickup())
                            .vehicle(gen.getVehicle())
                            .build())
                    .orderIndex(i)
                    .type(PICKUP)
                    .build());
            keys.add(Key.<IntegerRouting>builder()
                    .value(IntegerRouting.builder()
                            .randomKey(gen.getDelivery())
                            .vehicle(gen.getVehicle())
                            .build())
                    .orderIndex(i)
                    .type(DELIVERY)
                    .build());
        }
        keys.sort(Comparator.comparing(Key::getValue));
        return keys;
    }

}
