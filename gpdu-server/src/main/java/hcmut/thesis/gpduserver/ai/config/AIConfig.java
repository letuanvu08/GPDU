package hcmut.thesis.gpduserver.ai.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIConfig {
    private Integer populationSize;
    private Integer travelCost;
    private Integer waitingCost;
    private Integer lateCost;
    private Integer tournamentSize;
    private float elitismRate;
    private float crossover;
    private float mutation;
    private Integer maxGeneration;
}
