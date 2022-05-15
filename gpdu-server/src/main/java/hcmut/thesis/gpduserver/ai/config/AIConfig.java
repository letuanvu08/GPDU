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
    private int tournamentSize = 3;
    @Builder.Default
    private float elitismRate = 0.01f;
    @Builder.Default
    private float crossover = 0.8f;
    @Builder.Default
    private float mutation = 0.5f;
    @Builder.Default
    private int maxGeneration = 500;
    @Builder.Default
    private float swapVehicle = 0.6f;
    @Builder.Default
    private long startTime = System.currentTimeMillis() / 1000;

}
