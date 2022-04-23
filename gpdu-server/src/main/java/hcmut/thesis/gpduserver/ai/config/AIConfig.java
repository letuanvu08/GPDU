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
    private int populationSize;
    private float travelCost;
    private float waitingCost;
    private float lateCost;
    private int tournamentSize;
    private float elitismRate;
    private float crossover;
    private float mutation;
    private int maxGeneration;
    private long startTime;

}
