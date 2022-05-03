package hcmut.thesis.gpduserver.ai.models;

import hcmut.thesis.gpduserver.constants.enumations.TypeNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutingMatrix {
    private Integer vehicleNumber;
    private Integer orderNumber;
    private List<List<Float>> vehicleMatrix;
    private List<List<Float>> orderNodeMatrix;

    public Float getDurationVehicle(Integer vehicleIndex, Integer orderIndex, TypeNode type) {
        return vehicleMatrix.get(vehicleIndex).get(orderIndex * 2 + type.ordinal());
    }

    public Float getDurationOrder(Integer fromOrder, TypeNode fromType, Integer toOrder, TypeNode toType) {
        return orderNodeMatrix.get(fromOrder * 2 + fromType.ordinal()).get(toOrder * 2 + toType.ordinal());
    }


}
