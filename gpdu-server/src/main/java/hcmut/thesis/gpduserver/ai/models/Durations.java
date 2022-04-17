package hcmut.thesis.gpduserver.ai.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Durations {
    private float travel;
    private float waiting;
    private float late;
}
