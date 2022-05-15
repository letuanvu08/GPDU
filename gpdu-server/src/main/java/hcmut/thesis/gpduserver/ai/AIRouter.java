package hcmut.thesis.gpduserver.ai;

import hcmut.thesis.gpduserver.ai.config.AIConfig;
import hcmut.thesis.gpduserver.ai.config.Cost;
import hcmut.thesis.gpduserver.ai.models.*;
import hcmut.thesis.gpduserver.ai.models.Chromosome.Gen;
import hcmut.thesis.gpduserver.ai.utils.GeneticOperation;
import hcmut.thesis.gpduserver.ai.utils.RandomKey;

import java.util.*;

import hcmut.thesis.gpduserver.ai.utils.RoutingOperation;
import hcmut.thesis.gpduserver.models.entity.Location;
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
    private final Location repoLocation;

    public AIRouter(List<RoutingOrder> orders,
                    List<RoutingVehicle> vehicles, AIConfig config, RoutingMatrix routingMatrix,
                    Location repoLocation, Cost cost) {
        this.orders = orders;
        this.vehicles = vehicles;
        this.routingMatrix = routingMatrix;
        this.config = config;
        this.repoLocation = repoLocation;
        this.geneticOperation = new GeneticOperation(config, orders, routingMatrix, cost);
    }

    private RoutingResponse decodeChromosome(Chromosome chromosome) {
        List<Key<IntegerRouting>> keys = RoutingOperation.sortKey(chromosome.getGens());
        List<RoutingResponse.Route> routes = new ArrayList<>();
        List<RoutingKey> routingKeys = new ArrayList<>();
        Integer vehicle = keys.get(0).getValue().getVehicle();
        for (int i = 0; i < keys.size() - 1; i++) {
            RoutingKey temp = RoutingKey.builder()
                    .orderId(keys.get(i).getOrderIndex())
                    .type(keys.get(i).getType())
                    .build();
            if (keys.get(i).getValue().getVehicle() != vehicle) {
                RoutingResponse.Route route = RoutingResponse.Route.builder()
                        .vehicleId(vehicle)
                        .routingKeys(routingKeys)
                        .build();
                routes.add(route);
                routingKeys = new ArrayList<>();
                routingKeys.add(temp);
                vehicle = keys.get(i).getValue().getVehicle();
            } else {
                routingKeys.add(temp);
            }
        }
        RoutingKey temp = RoutingKey.builder()
                .orderId(keys.get(keys.size() - 1).getOrderIndex())
                .type(keys.get(keys.size() - 1).getType())
                .build();
        routingKeys.add(temp);
        RoutingResponse.Route route = RoutingResponse.Route.builder()
                .vehicleId(vehicle)
                .routingKeys(routingKeys)
                .build();
        routes.add(route);
        return RoutingResponse.builder()
                .routes(routes)
                .build();
    }

    @Override
    public RoutingResponse routing() {
//        List<Key<RoutingOrder.RoutingNode>> keys = getListKeySortNodeByEarliestTime(orders);
        List<Chromosome> population = this.initPopulation();
        int generation = 0;
        while (generation < config.getMaxGeneration()) {
            population = geneticOperation.evolve(population, vehicles.size(), routingMatrix);
            log.info("Generation: {}", generation);
            Chromosome best = population.get(0);
            log.info("Best individual in generation has fitness: {}, travelTime: {}, waitingTime: {}, lateTime: {}",
                best.getFitness(),best.getDurations().getTravel(), best.getDurations().getWaiting(), best.getDurations().getLate());
            generation += 1;
        }

        return decodeChromosome(population.get(0));
    }

    private List<Chromosome> initPopulation() {

        List<Chromosome> result = new ArrayList<>();
        for (int i = 0; i < config.getPopulationSize(); i++) {
            List<Gen> sample = this.getSample(orders);
            Chromosome chromosome = Chromosome.builder()
                    .gens(sample)
                    .build();
            geneticOperation.calFitness(chromosome);
            result.add(chromosome);
        }
        result.sort(Chromosome::compareTo);
        return result;
    }

    private List<Gen> getSample(List<RoutingOrder> orders){
        List<Gen> sample = new ArrayList<>();
        for(int i = 0; i< orders.size();i++){
            RoutingOrder order = orders.get(i);
            Integer vehicleId = RandomKey.generateInSize(vehicles.size());
            boolean vehicleConstant = false;
            if (order.getVehicleConstant()) {
                vehicleId = order.getVehicleId();
                vehicleConstant = true;
            }
            Gen gen = Gen.builder()
                .pickup(RandomKey.generateInSize(10000))
                .delivery(RandomKey.generateInSize(10000))
                .vehicle(vehicleId)
                .vehicleConstant(vehicleConstant)
                .build();
            sample.add(gen);
        }
        return sample;
    }

//    private List<Gen> getSample(List<Key<RoutingOrder.RoutingNode>> keys,
//                                List<RoutingOrder> orders) {
//        List<Gen> sample = new ArrayList<>();
//        List<Integer> sampleRandom = getSampleKeyRandom(keys.size());
//        for (int i = 0; i < orders.size(); i++) {
//            RoutingOrder order = orders.get(i);
//            Integer vehicleId = RandomKey.generateInSize(vehicles.size());
//            boolean vehicleConstant = false;
//            if (order.getVehicleConstant()) {
//                vehicleId = order.getVehicleId();
//                vehicleConstant = true;
//            }
//            int finalI = i;
//            Gen gene = Gen.builder()
//                    .pickup(sampleRandom.get(
//                            keys.indexOf(keys.stream().filter(
//                                            key -> key.getOrderIndex() == finalI && key.getType().equals(PICKUP))
//                                    .findFirst().orElse(null))))
//                    .delivery(sampleRandom.get(
//                            keys.indexOf(keys.stream().filter(
//                                            key -> key.getOrderIndex() == finalI && key.getType()
//                                                    .equals(DELIVERY))
//                                    .findFirst().orElse(null))))
//                    .vehicle(vehicleId)
//                    .vehicleConstant(vehicleConstant)
//                    .build();
//            sample.add(gene);
//        }
//        return sample;
//    }

//    private List<Integer> getSampleKeyRandom(int size) {
//        List<Integer> sampleRandom = new ArrayList<>();
//        int prevRandom = INIT_RANDOM;
//        while (sampleRandom.size() < size) {
//            Integer currentRandom = RandomKey.generateBaseMin(prevRandom, BOUND_RANDOM_KEY);
//            sampleRandom.add(currentRandom);
//            prevRandom = currentRandom;
//        }
//        return sampleRandom;
//    }


//    private List<Key<RoutingOrder.RoutingNode>> getListKeySortNodeByEarliestTime(
//            List<RoutingOrder> orders) {
//        List<Key<RoutingOrder.RoutingNode>> keys = new ArrayList<>();
//        for (int i = 0; i < orders.size(); i++) {
//            RoutingOrder order = orders.get(i);
//            keys.add(Key.<RoutingOrder.RoutingNode>builder()
//                    .orderIndex(i)
//                    .type(PICKUP)
//                    .value(order.getPickup())
//                    .build());
//            keys.add(Key.<RoutingOrder.RoutingNode>builder()
//                    .orderIndex(i)
//                    .type(DELIVERY)
//                    .value(order.getDelivery())
//                    .build());
//        }
//        keys.sort(Comparator.comparing(Key::getValue));
//        return keys;
//    }

}
