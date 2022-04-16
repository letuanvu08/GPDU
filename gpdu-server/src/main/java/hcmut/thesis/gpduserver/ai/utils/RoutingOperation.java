package hcmut.thesis.gpduserver.ai.utils;

import hcmut.thesis.gpduserver.ai.models.IntegerRouting;
import hcmut.thesis.gpduserver.ai.models.Key;
import hcmut.thesis.gpduserver.ai.models.RoutingMatrix;
import java.util.List;

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

}
