package hcmut.thesis.gpduserver.ai.models;


import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chromosome implements Comparable<Chromosome> {

    private List<Gen> gens;
    private Float fitness = 0f;


    @Override
    public int compareTo(Chromosome other) {
        float sub = this.getFitness() - other.getFitness();
        if (sub > 0) {
            return 1;
        } else if (sub < 0) {
            return -1;
        }
        return 0;
    }

    public Chromosome clone(){
        List<Gen> gens = new ArrayList<>();
        this.getGens().forEach(gen -> gens.add(gen.clone()));
        return Chromosome.builder()
            .gens(gens)
            .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Gen {

        private Integer pickup;
        private Integer delivery;
        private Integer vehicle;
        private Boolean vehicleConstant;

        public Gen clone() {
            return Gen.builder()
                .pickup(pickup)
                .delivery(delivery)
                .vehicleConstant(vehicleConstant)
                .vehicle(vehicle)
                .build();
        }
    }

}
