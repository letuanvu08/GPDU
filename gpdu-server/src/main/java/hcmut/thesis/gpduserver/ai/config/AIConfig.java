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
    private int tournamentSize = 5;
    @Builder.Default
    private float elitismRate = 0.05f;
    @Builder.Default
    private float crossover = 0.7f;
    @Builder.Default
    private float mutation = 0.3f;
    @Builder.Default
    private int maxGeneration = 10000;
    @Builder.Default
    private float swapVehicle = 0.6f;

}
