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
    private List<List<Float>> orderNodeMatrix;
    private List<List<Float>> vehicleMatrix;
    private List<Float> repoList;
    private List<Float> vehicleRepoList;
}
