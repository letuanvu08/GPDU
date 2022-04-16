package hcmut.thesis.gpduserver.ai;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.DELIVERY;
import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

import hcmut.thesis.gpduserver.ai.config.AIConfig;
import hcmut.thesis.gpduserver.ai.models.*;
import hcmut.thesis.gpduserver.ai.models.Chromosome.Gen;
import hcmut.thesis.gpduserver.ai.utils.GeneticOperation;
import hcmut.thesis.gpduserver.ai.utils.RandomKey;

import java.util.*;

import hcmut.thesis.gpduserver.ai.utils.RoutingOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AIRouter implements IAIRouter {

    private final List<RoutingOrder> orders;
    private final List<RoutingVehicle> vehicles;
    private static final Integer BOUND_RANDOM_KEY = 1000;
    private static final Integer INIT_RANDOM = 1000;
    private final AIConfig config;
    private final RoutingMatrix routingMatrix;
    private final GeneticOperation geneticOperation;

    public AIRouter(List<RoutingOrder> orders,
                    List<RoutingVehicle> vehicles, AIConfig config, RoutingMatrix routingMatrix
    ) {
        this.orders = orders;
        this.vehicles = vehicles;
        this.routingMatrix = routingMatrix;
        this.config = config;
        this.geneticOperation = new GeneticOperation(config);
    }

    private RoutingResponse decodeChromosome(Chromosome chromosome) {
        List<Key<IntegerRouting>> keys = RoutingOperation.sortKey(chromosome.getGens());
        List<RoutingResponse.Route> routes = new ArrayList<>();
        List<RoutingKey> routingKeys = new ArrayList<>();
        Integer vehicle = keys.get(0).getValue().getVehicle();
        for (Key<IntegerRouting> key : keys) {
            RoutingKey temp = RoutingKey.builder()
                    .orderId(key.getOrderIndex())
                    .type(key.getType())
                    .build();
            if (key.getValue().getVehicle() != vehicle) {
                RoutingResponse.Route route = RoutingResponse.Route.builder()
                        .vehicleId(vehicle)
                        .routingKeys(routingKeys)
                        .build();
                routes.add(route);
                routingKeys = new ArrayList<>();
                routingKeys.add(temp);
            } else {
                routingKeys.add(temp);
            }
        }
        return RoutingResponse.builder()
                .routes(routes)
                .build();
    }

    @Override
    public RoutingResponse routing() {
        List<Key<RoutingOrder.RoutingNode>> keys = getListKeySortNodeByEarliestTime(orders);
        List<Chromosome> population = this.initPopulation(keys);
        int generation = 0;
        while (generation < config.getMaxGeneration()) {
            log.info("Generation: {}", generation);
            population = geneticOperation.evolve(population, vehicles.size(), routingMatrix);
            log.info("Best individual in generation: {} has fitness: {}", generation, population.get(0).getFitness());
            generation += 1;
        }

        return decodeChromosome(population.get(0));
    }

    private List<Chromosome> initPopulation(List<Key<RoutingOrder.RoutingNode>> keys) {

        List<Chromosome> result = new ArrayList<>();
        for (int i = 0; i < config.getPopulationSize(); i++) {
            List<Gen> sample = this.getSample(keys, orders);
            Chromosome chromosome = Chromosome.builder()
                    .gens(sample)
                    .fitness(geneticOperation.calFitness(sample, routingMatrix))
                    .build();
            result.add(chromosome);
        }
        result.sort(Chromosome::compareTo);
        return result;
    }

    private List<Gen> getSample(List<Key<RoutingOrder.RoutingNode>> keys,
                                List<RoutingOrder> orders) {
        List<Gen> sample = new ArrayList<>();
        List<Integer> sampleRandom = getSampleKeyRandom(keys.size());
        for (int i = 0; i < orders.size(); i++) {
            RoutingOrder order = orders.get(i);
            Integer vehicleId = RandomKey.generateInSize(vehicles.size());
            boolean vehicleConstant = false;
            if (order.getVehicleConstant()) {
                vehicleId = order.getVehicleId();
                vehicleConstant = true;
            }
            int finalI = i;
            Gen gene = Gen.builder()
                    .pickup(sampleRandom.get(
                            keys.indexOf(keys.stream().filter(
                                            key -> key.getOrderIndex() == finalI && key.getType().equals(PICKUP))
                                    .findFirst().orElse(null))))
                    .delivery(sampleRandom.get(
                            keys.indexOf(keys.stream().filter(
                                            key -> key.getOrderIndex() == finalI && key.getType()
                                                    .equals(DELIVERY))
                                    .findFirst().orElse(null))))
                    .vehicle(vehicleId)
                    .vehicleConstant(vehicleConstant)
                    .build();
            sample.add(gene);
        }
        return sample;
    }

    private List<Integer> getSampleKeyRandom(int size) {
        List<Integer> sampleRandom = new ArrayList<>();
        int prevRandom = INIT_RANDOM;
        while (sampleRandom.size() < size) {
            Integer currentRandom = RandomKey.generateBaseMin(prevRandom, BOUND_RANDOM_KEY);
            sampleRandom.add(currentRandom);
            prevRandom = currentRandom;
        }
        return sampleRandom;
    }


    private List<Key<RoutingOrder.RoutingNode>> getListKeySortNodeByEarliestTime(
            List<RoutingOrder> orders) {
        List<Key<RoutingOrder.RoutingNode>> keys = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            RoutingOrder order = orders.get(i);
            keys.add(Key.<RoutingOrder.RoutingNode>builder()
                    .orderIndex(i)
                    .type(PICKUP)
                    .value(order.getPickup())
                    .build());
            keys.add(Key.<RoutingOrder.RoutingNode>builder()
                    .orderIndex(i)
                    .type(DELIVERY)
                    .value(order.getPickup())
                    .build());
        }
        keys.sort(Comparator.comparing(Key::getValue));
        return keys;
    }

}
