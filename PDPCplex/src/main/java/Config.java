import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config {
    private List<List<Float>> orderDurationMatrix;
    private List<List<Float>> vehicleDurationMatrix;
    private float waitingCost;
    private float lateCost;
    private float travelCost;
    private int vehicleNumber;
    private int orderNumber;
}
