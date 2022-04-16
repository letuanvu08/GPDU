package hcmut.thesis.gpduserver.ai.utils;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.DELIVERY;
import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

import hcmut.thesis.gpduserver.ai.models.Chromosome;
import hcmut.thesis.gpduserver.ai.models.Chromosome.Gene;
import hcmut.thesis.gpduserver.ai.models.IntegerRouting;
import hcmut.thesis.gpduserver.ai.models.Key;
import hcmut.thesis.gpduserver.mapbox.IMapboxClient;
import hcmut.thesis.gpduserver.models.entity.Order;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.modelmapper.internal.Pair;

public class GeneticOperation {

    private static final int GENERATION_MAX = 50;
    private static final int TRAVEL_COST = 200;
    private static final int WAITING_COST = 10;
    private static final int LATE_COST = 20;
    private static final int TOURNAMENT_SIZE = 20;


    public static float calFitness(List<Gene> genes, List<Order> orders,
        IMapboxClient mapboxClient) {
        List<Key<IntegerRouting>> keys = new ArrayList<>();
        for (int i = 0; i < genes.size(); i++) {
            keys.add(Key.<IntegerRouting>builder()
                .value(genes.get(i).getPickup())
                .orderIndex(i)
                .type(PICKUP.name())
                .build());
            keys.add(Key.<IntegerRouting>builder()
                .value(genes.get(i).getDelivery())
                .orderIndex(i)
                .type(DELIVERY.name())
                .build());
        }
        keys.sort(Comparator.comparing(Key::getValue));
        float duration = RoutingOperation.calTotalDuration(keys, orders, mapboxClient);
        return duration * TRAVEL_COST;
    }

    public static Pair<Chromosome, Chromosome> choosePairParent(List<Chromosome> population) {
        Chromosome parent1 = tournamentSelection(population);
        Chromosome parent2 = tournamentSelection(population);
        while (parent2 == parent1) {
            parent2 = tournamentSelection(population);
        }
        return Pair.of(parent1, parent2);
    }

    private static Chromosome tournamentSelection(List<Chromosome> population) {
        int size = population.size();
        int random = RandomKey.generateInSize(size);
        Chromosome best = population.get(random);
        int tournament = 0;
        while (tournament < TOURNAMENT_SIZE) {
            random = RandomKey.generateInSize(size);
            Chromosome current = population.get(random);
            if (best.getFitness() < current.getFitness()) {
                best = current;
            }
            tournament++;
        }
        return best;
    }

    public static Chromosome mate(Chromosome c1, Chromosome c2) {
        return null;
    }

    public static Chromosome mutate() {
        return null;
    }
}
