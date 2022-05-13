package hcmut.thesis.gpduserver.ai.utils;

import hcmut.thesis.gpduserver.ai.config.AIConfig;
import hcmut.thesis.gpduserver.ai.config.Cost;
import hcmut.thesis.gpduserver.ai.models.*;
import hcmut.thesis.gpduserver.ai.models.Chromosome.Gen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.internal.Pair;

public class GeneticOperation {


    private final AIConfig config;
    private final List<RoutingOrder> routingOrders;
    private final List<RoutingVehicle> vehicles;
    private final RoutingMatrix routingMatrix;
    private final Cost cost;

    public GeneticOperation(AIConfig config, List<RoutingOrder> routingOrders,
                            List<RoutingVehicle> vehicles, RoutingMatrix routingMatrix, Cost cost) {
        this.config = config;
        this.routingOrders = routingOrders;
        this.vehicles = vehicles;
        this.routingMatrix = routingMatrix;
        this.cost = cost;
    }

    public List<Chromosome> evolve(List<Chromosome> population, int numberVehicle,
                                   RoutingMatrix routingMatrix) {
        int size = population.size();
        int index = (int) ((float) size * config.getElitismRate());
        List<Chromosome> buf = population.subList(0,index).stream().map(chromosome -> chromosome).collect(Collectors.toList());
        while (index < size) {
            if (ThreadLocalRandom.current().nextFloat() <= config.getCrossover()) {
                Pair<Chromosome, Chromosome> pairParent = choosePairParent(population);
                List<Chromosome> candidates = new ArrayList<>(
                        List.of(pairParent.getLeft(), pairParent.getRight()));
                List<Chromosome> children = mate(pairParent.getLeft(), pairParent.getRight(),
                        routingMatrix);
                children = children.stream().map(child -> {
                    if (ThreadLocalRandom.current().nextFloat() <= config.getMutation()) {
                        return mutate(child, routingMatrix);
                    }
                    return child;
                }).collect(Collectors.toList());
                candidates.addAll(children);
                candidates.sort(Chromosome::compareTo);
                buf.addAll(candidates.subList(0, 2));
                index += 2;
            } else {
                Chromosome luckyMan = population.get(RandomKey.random(index, size)).clone();
                if (ThreadLocalRandom.current().nextFloat() <= config.getMutation()) {
                    luckyMan = mutate(luckyMan, routingMatrix);
                }
                calFitness(luckyMan);
                index += 1;
                buf.add(luckyMan);
            }
        }
        buf.sort(Chromosome::compareTo);
        population.clear();
        return buf.subList(0, config.getPopulationSize()).stream().map(chromosome -> chromosome).collect(
            Collectors.toList());
    }

    public void calFitness(Chromosome chromosome) {
        List<Gen> gens = chromosome.getGens();
        List<Key<IntegerRouting>> keys = RoutingOperation.sortKey(gens);
        Durations durations = RoutingOperation.calDurations(keys, routingMatrix, routingOrders, config.getStartTime());

         float fitness = durations.getTravel() * cost.getTravel() +
            durations.getLate() * cost.getLate() +
            durations.getWaiting() * cost.getWaiting();
         chromosome.setFitness(fitness);
         chromosome.setDurations(durations);

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
            if (best.getFitness() > current.getFitness()) {
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
            .build();
        calFitness(child1);
        Chromosome child2 = Chromosome.builder()
            .gens(gensChild2)
            .build();
        calFitness(child2);
        return List.of(child1, child2);
    }

    @SafeVarargs
    private List<Gen> concatGen(List<Gen>... listGens) {
        List<Gen> newGen = new ArrayList<>();
        Stream.of(listGens).forEach(gens -> gens.forEach(gen -> newGen.add(gen.clone())));
        return newGen;
    }

    public Chromosome mutate(Chromosome chromosome, RoutingMatrix routingMatrix) {
        if (ThreadLocalRandom.current().nextFloat() <= config.getSwapVehicle()) {
            Pair<Gen, Gen> pairGen = chooseGenMutation(chromosome);
            swapVehicleGen(pairGen.getLeft(), pairGen.getRight());
            calFitness(chromosome);
            return chromosome;
        }
        int genIndex = RandomKey.random(0, chromosome.getGens().size());
        Gen gen = chromosome.getGens().get(genIndex);
        gen.setDelivery(RandomKey.generateInSize(10000));
        gen.setPickup(RandomKey.generateInSize(10000));
        calFitness(chromosome);
        return chromosome;
    }

    private void swapVehicleGen(Gen gen1, Gen gen2) {
        Integer vehicle1 = gen1.getVehicle();
        Integer vehicle2 = gen2.getVehicle();
        gen1.setVehicle(vehicle2);
        gen2.setVehicle(vehicle1);
    }

    private Pair<Gen, Gen> chooseGenMutation(Chromosome chromosome) {
        int indexGen1 = RandomKey.random(0, chromosome.getGens().size());
        while (chromosome.getGens().get(indexGen1).getVehicleConstant()) {
            indexGen1 = RandomKey.random(0, chromosome.getGens().size());
        }
        int indexGen2 = RandomKey.random(0, chromosome.getGens().size());
        while (chromosome.getGens().get(indexGen2).getVehicleConstant()
                && indexGen2 != indexGen1
                && chromosome.getGens().get(indexGen1).getVehicle()
                .equals(chromosome.getGens().get(indexGen2).getVehicle())) {
            indexGen2 = RandomKey.random(0, chromosome.getGens().size());
        }
        return Pair.of(chromosome.getGens().get(indexGen1), chromosome.getGens().get(indexGen2));
    }

}
