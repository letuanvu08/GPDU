package hcmut.thesis.gpduserver.ai.utils;

import hcmut.thesis.gpduserver.ai.config.AIConfig;
import hcmut.thesis.gpduserver.ai.models.Chromosome;
import hcmut.thesis.gpduserver.ai.models.Chromosome.Gen;
import hcmut.thesis.gpduserver.ai.models.IntegerRouting;
import hcmut.thesis.gpduserver.ai.models.Key;
import hcmut.thesis.gpduserver.ai.models.RoutingMatrix;
import hcmut.thesis.gpduserver.ai.models.RoutingOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.internal.Pair;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.DELIVERY;
import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

public class GeneticOperation {


    private AIConfig config;

    public GeneticOperation(AIConfig config) {
        this.config = config;
    }

    public float calFitness(List<Gen> gens, RoutingMatrix routingMatrix) {
        List<Key<IntegerRouting>> keys = new ArrayList<>();
        for (int i = 0; i < gens.size(); i++) {
            Gen gen = gens.get(i);
            keys.add(Key.<IntegerRouting>builder()
                .value(IntegerRouting.builder()
                    .randomKey(gen.getPickup())
                    .vehicle(gen.getVehicle())
                    .build())
                .orderIndex(i)
                .type(PICKUP)
                .build());
            keys.add(Key.<IntegerRouting>builder()
                .value(IntegerRouting.builder()
                    .randomKey(gen.getDelivery())
                    .vehicle(gen.getVehicle())
                    .build())
                .orderIndex(i)
                .type(DELIVERY)
                .build());
        }
        keys.sort(Comparator.comparing(Key::getValue));
        float duration = RoutingOperation.calTotalDuration(keys, routingMatrix);
        return duration * config.getTravelCost();
    }

    public Pair<Chromosome, Chromosome> choosePairParent(List<Chromosome> population) {
        Chromosome parent1 = tournamentSelection(population);
        Chromosome parent2 = tournamentSelection(population);
        while (parent2 == parent1) {
            parent2 = tournamentSelection(population);
        }
        return Pair.of(parent1, parent2);
    }

    private Chromosome tournamentSelection(List<Chromosome> population) {
        int size = population.size();
        int random = RandomKey.generateInSize(size);
        Chromosome best = population.get(random);
        int tournament = 0;
        while (tournament < config.getTournamentSize()) {
            random = RandomKey.generateInSize(size);
            Chromosome current = population.get(random);
            if (best.getFitness() < current.getFitness()) {
                best = current;
            }
            tournament++;
        }
        return best;
    }

    public Pair<Chromosome, Chromosome> mate(Chromosome c1, Chromosome c2) {
        int size = c1.getGens().size();
        List<Gen> genC1 = c1.getGens();
        List<Gen> genC2 = c2.getGens();
        int index1 = RandomKey.random(1, size - 3);
        int index2 = RandomKey.random(index1, size - 2);
        Chromosome child1 = new Chromosome();
        Chromosome child2 = new Chromosome();
        List<Gen> gensChild1 = concatGen(genC1.subList(0, index1), genC2.subList(index1, index2),
            genC1.subList(index2, size));
        List<Gen> gensChild2 = concatGen(genC2.subList(0, index1), genC1.subList(index1, index2),
            genC2.subList(index2, size));
        child1.setGens(gensChild1);
        child2.setGens(gensChild2);
        return Pair.of(child1, child2);

    }

    @SafeVarargs
    private List<Gen> concatGen(List<Gen>... listGens) {
        return Stream.of(listGens).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Chromosome mutate(Chromosome chromosome, int numberOfVehicle) {
        int indexGen = RandomKey.random(0, chromosome.getGens().size());
        while (chromosome.getGens().get(indexGen).getVehicleConstant()) {
            indexGen = RandomKey.random(0, chromosome.getGens().size());
        }
        int vehicleId = RandomKey.generateInSize(numberOfVehicle);
        chromosome.getGens().get(indexGen).setVehicle(vehicleId);
        return chromosome;
    }

}
