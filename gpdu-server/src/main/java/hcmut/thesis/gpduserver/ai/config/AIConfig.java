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
    @Builder.Default
    private int populationSize = 1000;
    @Builder.Default
    private float travelCost = 2f;
    @Builder.Default
    private float waitingCost = 0.01f;
    @Builder.Default
    private float lateCost = 0.02f;
    @Builder.Default
    private int tournamentSize = 3;
    @Builder.Default
    private float elitismRate = 0.1f;
    @Builder.Default
    private float crossover = 0.5f;
    @Builder.Default
    private float mutation = 0.2f;
    @Builder.Default
    private int maxGeneration = 200;

}
