package hcmut.thesis.gpduserver.ai.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cost {
    @Builder.Default
    private float travel = 2f;
    @Builder.Default
    private float waiting = 0.01f;
    @Builder.Default
    private float late = 0.02f;
}
