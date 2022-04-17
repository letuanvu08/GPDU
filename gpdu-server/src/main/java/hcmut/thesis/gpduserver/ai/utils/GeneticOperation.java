package hcmut.thesis.gpduserver.ai.utils;

import hcmut.thesis.gpduserver.ai.config.AIConfig;
import hcmut.thesis.gpduserver.ai.models.*;
import hcmut.thesis.gpduserver.ai.models.Chromosome.Gen;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Routing;
import org.modelmapper.internal.Pair;

public class GeneticOperation {


    private final AIConfig config;
    private final List<RoutingOrder> routingOrders;

    public GeneticOperation(AIConfig config, List<RoutingOrder> routingOrders) {
        this.config = config;
        this.routingOrders = routingOrders;
    }

    public List<Chromosome> evolve(List<Chromosome> population, int numberVehicle, RoutingMatrix routingMatrix) {
        int size = population.size();
        int index = (int) ((float) size * config.getElitismRate());
        List<Chromosome> buf = population.subList(0, index);
        while (index < size) {
            if (ThreadLocalRandom.current().nextFloat() <= config.getCrossover()) {
                Pair<Chromosome, Chromosome> pairParent = choosePairParent(population);
                List<Chromosome> children = mate(pairParent.getLeft(), pairParent.getRight(), routingMatrix);
                children = children.stream().map(child -> {
                    if (ThreadLocalRandom.current().nextFloat() <= config.getMutation()) {
                        return mutate(child, numberVehicle, routingMatrix);
                    }
                    return child;
                }).collect(Collectors.toList());
                buf.addAll(children);
                index += 2;
            } else {
                Chromosome luckyMan = population.get(RandomKey.random(index, size));
                if (ThreadLocalRandom.current().nextFloat() <= config.getMutation()) {

                    luckyMan = mutate(luckyMan, numberVehicle, routingMatrix);
                    luckyMan.setFitness(calFitness(luckyMan.getGens(), routingMatrix, routingOrders));
                }
                index += 1;
                buf.add(luckyMan);
            }
        }
        buf.sort(Chromosome::compareTo);

        return buf.subList(0, config.getPopulationSize());
    }

    public float calFitness(List<Gen> gens, RoutingMatrix routingMatrix, List<RoutingOrder> routingOrders) {
        List<Key<IntegerRouting>> keys = RoutingOperation.sortKey(gens);
        Durations durations = RoutingOperation.calDurations(keys, routingMatrix, routingOrders);
        return durations.getTravel() * config.getTravelCost() +
                durations.getLate() * config.getLateCost() +
                durations.getWaiting() * config.getLateCost();
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

    public List<Chromosome> mate(Chromosome c1, Chromosome c2, RoutingMatrix routingMatrix) {
        int size = c1.getGens().size();
        List<Gen> genC1 = c1.getGens();
        List<Gen> genC2 = c2.getGens();
        int index1 = RandomKey.random(1, size - 2);
        int index2 = RandomKey.random(index1, size - 1);

        List<Gen> gensChild1 = concatGen(genC1.subList(0, index1), genC2.subList(index1, index2),
                genC1.subList(index2, size));
        List<Gen> gensChild2 = concatGen(genC2.subList(0, index1), genC1.subList(index1, index2),
                genC2.subList(index2, size));
        Chromosome child1 = Chromosome.builder()
                .gens(gensChild1)
                .fitness(calFitness(gensChild1, routingMatrix, routingOrders))
                .build();
        Chromosome child2 = Chromosome.builder()
                .gens(gensChild2)
                .fitness(calFitness(gensChild2, routingMatrix, routingOrders))
                .build();
        return List.of(child1, child2);

    }

    @SafeVarargs
    private List<Gen> concatGen(List<Gen>... listGens) {
        return Stream.of(listGens).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Chromosome mutate(Chromosome chromosome, int numberOfVehicle, RoutingMatrix routingMatrix) {
        int indexGen = RandomKey.random(0, chromosome.getGens().size());
        while (chromosome.getGens().get(indexGen).getVehicleConstant()) {
            indexGen = RandomKey.random(0, chromosome.getGens().size());
        }
        int vehicleId = RandomKey.generateInSize(numberOfVehicle);
        chromosome.getGens().get(indexGen).setVehicle(vehicleId);
        chromosome.setFitness(calFitness(chromosome.getGens(), routingMatrix, routingOrders));
        return chromosome;
    }


}
