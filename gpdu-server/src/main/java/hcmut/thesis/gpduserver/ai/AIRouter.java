package hcmut.thesis.gpduserver.ai;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.DELIVERY;
import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

import hcmut.thesis.gpduserver.ai.config.AIConfig;
import hcmut.thesis.gpduserver.ai.models.*;
import hcmut.thesis.gpduserver.ai.models.Chromosome.Gen;
import hcmut.thesis.gpduserver.ai.utils.GeneticOperation;
import hcmut.thesis.gpduserver.ai.utils.RandomKey;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.mapbox.IMapboxClient;
import hcmut.thesis.gpduserver.models.entity.Node;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

public class AIRouter implements IAIRouter {

    private List<RoutingOrder> orders;
    private List<RoutingVehicle> vehicles;
    private final Integer BOUND_RANDOM_KEY = 1000;
    private final Integer INIT_RANDOM = 1000;
    private final List<List<Float>> durationMatrix = new ArrayList<>();
    private AIConfig config;
    private RoutingMatrix routingMatrix;
    private GeneticOperation geneticOperation;

    public AIRouter(List<RoutingOrder> orders,
                    List<RoutingVehicle> vehicles, AIConfig config, RoutingMatrix routingMatrix) {
        this.orders = orders;
        this.vehicles = vehicles;
        this.routingMatrix = routingMatrix;
        this.config = config;
    }

    @Override
    public void routing() {
        List<Key<RoutingOrder.RoutingNode>> keys = getListKeySortNodeByEarliestTime(orders);
        List<Chromosome> population = this.initPopulation(keys);

    }

    private List<Chromosome> initPopulation(List<Key<RoutingOrder.RoutingNode>> keys) {

        List<Chromosome> result = new ArrayList<>();
        for (int i = 0; i < config.getPopulationSize(); i++) {
            List<Gen> sample = this.getSample(keys, orders);
            Chromosome chromosome = Chromosome.builder()
                    .gens(sample)
                    .fitness(geneticOperation.calFitness(sample, orders, durationMatrix))
                    .build();
            result.add(chromosome);
        }
        result.sort(((c1, c2) -> (int) Math.ceil(c1.getFitness() - c2.getFitness()) + 1));
        return result;
    }

    private List<Gen> getSample(List<Key<RoutingOrder.RoutingNode>> keys, List<RoutingOrder> orders) {
        List<Gen> sample = new ArrayList<>();
        List<Integer> sampleRandom = getSampleKeyRandom(keys.size());
        for (int i = 0; i < orders.size(); i++) {
            RoutingOrder order = orders.get(i);
            Integer vehicleId = RandomKey.generateInSize(vehicles.size());
            Boolean vehicleConstant = false;
            if (order.getVehicleConstant()) {
                vehicleId = order.getVehicleId();
                vehicleConstant = true;
            }
            int finalI = i;
            Gen gene = Gen.builder()
                    .pickup(IntegerRouting.builder()
                            .vehicle(vehicleId)
                            .randomKey(sampleRandom.get(
                                    keys.indexOf(keys.stream().filter(
                                                    key -> key.getOrderIndex() == finalI && key.getType().equals(PICKUP.name()))
                                            .findFirst().orElse(null))))
                            .build())
                    .delivery(IntegerRouting.builder()
                            .vehicle(vehicleId)
                            .randomKey(sampleRandom.get(
                                    keys.indexOf(keys.stream().filter(
                                                    key -> key.getOrderIndex() == finalI && key.getType().equals(DELIVERY.name()))
                                            .findFirst().orElse(null))))
                            .build())
                    .vehicleConstant(vehicleConstant)
                    .build();
            sample.add(gene);
        }
        return sample;
    }

    private List<Integer> getSampleKeyRandom(int size) {
        List<Integer> sampleRandom = new ArrayList<>();
        Integer prevRandom = INIT_RANDOM;
        while (sampleRandom.size() < size) {
            Integer currentRandom = RandomKey.generateBaseMin(prevRandom, BOUND_RANDOM_KEY);
            sampleRandom.add(currentRandom);
            prevRandom = currentRandom;
        }
        return sampleRandom;
    }


    private Chromosome execute(List<Chromosome> population) {
        return null;
    }

    private List<Key<RoutingOrder.RoutingNode>> getListKeySortNodeByEarliestTime(List<RoutingOrder> orders) {
        List<Key<RoutingOrder.RoutingNode>> keys = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            RoutingOrder order = orders.get(i);
            keys.add(Key.<RoutingOrder.RoutingNode>builder()
                    .orderIndex(i)
                    .type(PICKUP.name())
                    .value(order.getPickup())
                    .build());
            keys.add(Key.<RoutingOrder.RoutingNode>builder()
                    .orderIndex(i)
                    .type(DELIVERY.name())
                    .value(order.getPickup())
                    .build());
        }
        keys.sort(Comparator.comparing(Key::getValue));
        return keys;
    }

}
