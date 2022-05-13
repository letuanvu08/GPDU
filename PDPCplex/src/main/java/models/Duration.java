package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Duration {
    List<List<Float>> orderNodeMatrix;
    List<List<Float>> vehicleMatrix;
    List<Float> repoList;
}
