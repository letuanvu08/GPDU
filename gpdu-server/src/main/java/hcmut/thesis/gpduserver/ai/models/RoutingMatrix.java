package hcmut.thesis.gpduserver.ai.models;

import hcmut.thesis.gpduserver.constants.enumations.TypeNode;

import java.util.List;

public class RoutingMatrix {
    private List<List<Float>> value;
    private Integer vehicleNumber;
    private Integer orderNumber;


    public Float getDurationVehicle(Integer vehicleIndex, Integer orderIndex, TypeNode type) {
        return value.get(vehicleIndex).get(vehicleNumber - 1 + orderIndex * 2 + type.ordinal());
    }

    public Float getDurationOrder(Integer fromOrder, TypeNode fromType, Integer toOrder, TypeNode toType) {
        return value.get(vehicleNumber - 1 + fromOrder * 2 + fromType.ordinal()).get(vehicleNumber - 1 + toOrder * 2 + toType.ordinal());
    }


}
