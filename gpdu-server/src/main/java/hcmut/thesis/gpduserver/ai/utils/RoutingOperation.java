package hcmut.thesis.gpduserver.ai.utils;

import hcmut.thesis.gpduserver.ai.models.Chromosome;
import hcmut.thesis.gpduserver.ai.models.IntegerRouting;
import hcmut.thesis.gpduserver.ai.models.Key;
import hcmut.thesis.gpduserver.ai.models.RoutingMatrix;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.DELIVERY;
import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

public class RoutingOperation {


    public static float calTotalDuration(List<Key<IntegerRouting>> keys, RoutingMatrix routingMatrix) {
        int prevVehicle = 0;
        float duration = 0;
        for (int i = 0; i < keys.size() - 1; i++) {
            Key<IntegerRouting> currentKey = keys.get(i);
            Key<IntegerRouting> nextKey = keys.get(i);
            int currentVehicle = currentKey.getValue().getVehicle();
            if (prevVehicle != currentVehicle) {
                duration += routingMatrix.getDurationVehicle(currentVehicle,
                        currentKey.getOrderIndex(), currentKey.getType());
            }
            duration += routingMatrix.getDurationOrder(currentKey.getOrderIndex(),
                    currentKey.getType(), nextKey.getOrderIndex(), nextKey.getType());
        }

        return duration;
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
