package hcmut.thesis.gpduserver.ai.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chromosome {
    private List<Gene> genes;
    private Float fitness = 0f;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Gene {
        private Integer pickup;
        private Integer delivery;
    }

}
