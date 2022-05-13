package input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Input {
    private List<Order> orders;
    private List<Vehicle> vehicles;
    private Cost cost;
    private Location repoLocation;
    private Duration duration;

}
