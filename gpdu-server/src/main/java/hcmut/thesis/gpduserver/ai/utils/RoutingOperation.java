package hcmut.thesis.gpduserver.ai.utils;

import hcmut.thesis.gpduserver.ai.models.*;

import hcmut.thesis.gpduserver.ai.models.RoutingOrder.RoutingNode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.DELIVERY;
import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

public class RoutingOperation {


    public static Durations calDurations(List<Key<IntegerRouting>> keys,
        RoutingMatrix routingMatrix,
        List<RoutingOrder> routingOrders, List<Long> startTimeVehicle) {
        int prevVehicle = -1;
        float travelDuration = 0;
        float waitingDuration = 0;
        float lateDuration = 0;
        float vehicleDuration = 0;
        for (int i = 0; i < keys.size() - 1; i++) {
            Key<IntegerRouting> currentKey = keys.get(i);
            Key<IntegerRouting> nextKey = keys.get(i + 1);
            int currentVehicle = currentKey.getValue().getVehicle();
            Long startTime = startTimeVehicle.get(currentVehicle);
            if (prevVehicle != currentVehicle) {
                int skipVehicleNumber = currentVehicle - prevVehicle - 1;
                for (int j = 0; j < skipVehicleNumber; j++) {
                    travelDuration += routingMatrix.getDurationVehicleRepo(prevVehicle + 1 + j);
                }
                float tempDuration = routingMatrix.getDurationVehicle(currentVehicle,
                    currentKey.getOrderIndex(), currentKey.getType());
                travelDuration += tempDuration;
                vehicleDuration = tempDuration;
                RoutingOrder.RoutingNode node = currentKey.getType().equals(PICKUP) ?
                    routingOrders.get(currentKey.getOrderIndex()).getPickup() :
                    routingOrders.get(currentKey.getOrderIndex()).getDelivery();
                if (startTime + vehicleDuration < node.getEarliestTime()) {
                    waitingDuration += (node.getEarliestTime() - startTime) - vehicleDuration;
                    vehicleDuration = (node.getEarliestTime() - startTime);
                }

                if (startTime + vehicleDuration > node.getLatestTime()) {
                    lateDuration += vehicleDuration - (node.getLatestTime() - startTime);

                }
                prevVehicle = currentVehicle;
            }
            if (currentKey.getValue().getVehicle() == nextKey.getValue().getVehicle()) {
                float tempDuration = routingMatrix.getDurationOrder(currentKey.getOrderIndex(),
                    currentKey.getType(), nextKey.getOrderIndex(), nextKey.getType());
                travelDuration += tempDuration;
                vehicleDuration += tempDuration;
                RoutingOrder.RoutingNode node = nextKey.getType().equals(PICKUP) ?
                    routingOrders.get(nextKey.getOrderIndex()).getPickup() :
                    routingOrders.get(nextKey.getOrderIndex()).getDelivery();
                if (startTime + vehicleDuration < node.getEarliestTime()) {
                    waitingDuration += (node.getEarliestTime() - startTime) - vehicleDuration;
                    vehicleDuration = (node.getEarliestTime() - startTime);
                }
                if (startTime + vehicleDuration > node.getLatestTime()) {
                    lateDuration += vehicleDuration + (startTime - node.getLatestTime());
                }
            } else {
                travelDuration += routingMatrix.getDurationRepo(currentKey.getOrderIndex(),
                    currentKey.getType());
            }
        }
        Key<IntegerRouting> finalKey = keys.get(keys.size() - 1);
        travelDuration += routingMatrix.getDurationRepo(finalKey.getOrderIndex(),
            finalKey.getType());
        for (int i = finalKey.getValue().getVehicle() + 1;
            i < routingMatrix.getVehicleMatrix().size(); i++) {
            travelDuration += routingMatrix.getDurationVehicleRepo(i);
        }
        return Durations.builder()
            .travel(travelDuration)
            .late(lateDuration)
            .waiting(waitingDuration)
            .build();
    }

    public static List<List<Float>> calDurationsTimeDoneNodes(List<Key<IntegerRouting>> keys,
        RoutingMatrix routingMatrix,
        List<RoutingOrder> routingOrders, List<Long> startTimeVehicle) {
        List<List<Float>> timeDoneNodes = new ArrayList<>();
        List<Float> timeDoneNode = null;
        Key<IntegerRouting> prev = null;
        int i = 1;
        while (i < keys.size()) {
            Key<IntegerRouting> currentKey = keys.get(i);
            List<Key<IntegerRouting>> sameVehicle = new ArrayList<>();
            if (prev == null) {
                prev = keys.get(0);
                sameVehicle.add(prev);
                i++;
            }
            while (currentKey.getValue().getVehicle() == prev.getValue().getVehicle()) {
                sameVehicle.add(currentKey);
                i++;
            }
            timeDoneNodes.add(
                calDurationsTimeDoneNode(keys, routingMatrix, routingOrders,
                    startTimeVehicle.get(prev.getValue().getVehicle())));
            prev = currentKey;
            sameVehicle = new ArrayList<>();
            sameVehicle.add(prev);
        }
        return timeDoneNodes;
    }

    private static List<Float> calDurationsTimeDoneNode(List<Key<IntegerRouting>> keys,
        RoutingMatrix routingMatrix,
        List<RoutingOrder> routingOrders, long startTime) {
        List<Float> timeDoneNode = new ArrayList<>();
        if (keys.size() == 0) {
            return timeDoneNode;
        }
        Key<IntegerRouting> prev = null;
        Float vehicleTime = (float) startTime;
        Float travelTime = 0f;
        for (Key<IntegerRouting> key : keys) {
            if (prev == null) {
                prev = key;
                int vehicleId = prev.getValue().getVehicle();
                travelTime = routingMatrix.getDurationVehicle(vehicleId,
                    prev.getOrderIndex(), prev.getType());
            } else {
                travelTime = routingMatrix.getDurationOrder(prev.getOrderIndex(), prev.getType(),
                    key.getOrderIndex(), key.getType());
            }
            vehicleTime += travelTime;
            RoutingNode node = getNodeInOrderByKey(key, routingOrders);
            if (vehicleTime < node.getEarliestTime()) {
                vehicleTime = (float) node.getEarliestTime();
            }
            timeDoneNode.add(vehicleTime);
        }
        return timeDoneNode;
    }

    private static RoutingOrder.RoutingNode getNodeInOrderByKey(Key<IntegerRouting> key,
        List<RoutingOrder> routingOrders) {
        return key.getType().equals(PICKUP) ?
            routingOrders.get(key.getOrderIndex()).getPickup() :
            routingOrders.get(key.getOrderIndex()).getDelivery();
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
