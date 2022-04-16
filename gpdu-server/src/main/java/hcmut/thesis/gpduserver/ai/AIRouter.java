package hcmut.thesis.gpduserver.ai;

import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.DELIVERY;
import static hcmut.thesis.gpduserver.constants.enumations.TypeNode.PICKUP;

import hcmut.thesis.gpduserver.ai.models.Chromosome;
import hcmut.thesis.gpduserver.ai.models.Chromosome.Gen;
import hcmut.thesis.gpduserver.ai.models.IntegerRouting;
import hcmut.thesis.gpduserver.ai.models.Key;
import hcmut.thesis.gpduserver.ai.utils.GeneticOperation;
import hcmut.thesis.gpduserver.ai.utils.RandomKey;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.mapbox.IMapboxClient;
import hcmut.thesis.gpduserver.models.entity.Node;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class AIRouter implements IAIRouter {

    private List<Order> orders;
    private List<Vehicle> vehicles;
    @Autowired
    private IMapboxClient mapboxClient;


    private final int POPULATION_SIZE = 50;
    private final Integer BOUND_RANDOM_KEY = 1000;
    private final Integer INIT_RANDOM = 1000;

    public AIRouter(List<Order> orders,
        List<Vehicle> vehicles, IMapboxClient mapboxClient) {
        this.orders = orders;
        this.vehicles = vehicles;
        this.mapboxClient = mapboxClient;
    }

    @Override
    public void routing() {
        List<Key<Node>> keys = getListKeySortNodeByEarliestTime(orders);
        List<String> vehicleIds = vehicles.stream()
            .map(v -> v.getId().toHexString())
            .collect(Collectors.toList());

        List<Chromosome> population = this.initPopulation(vehicleIds, keys);

    }

    private List<Chromosome> initPopulation(List<String> vehicleIds, List<Key<Node>> keys) {

        List<Chromosome> result = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Gen> sample = this.getSample(vehicleIds, keys, orders);
            Chromosome chromosome = Chromosome.builder()
                .gens(sample)
                .fitness(GeneticOperation.calFitness(sample, orders,mapboxClient))
                .build();
            result.add(chromosome);
        }
        result.sort(((c1, c2) -> (int) Math.ceil(c1.getFitness() - c2.getFitness()) + 1));
        return result;
    }

    private List<Gen> getSample(List<String> vehicleIds, List<Key<Node>> keys , List<Order> orders) {
        List<Gen> sample = new ArrayList<>();
        List<Integer> sampleRandom = getSampleKeyRandom(keys.size());
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            Integer vehicleId = RandomKey.generateInSize(vehicleIds.size());
            Boolean vehicleConstant = false;
            if (!StepOrderEnum.ORDER_RECEIVED.name().equals(order.getCurrentStep().getStatus())) {
                vehicleId = vehicleIds.indexOf(order.getVehicleId());
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


    private Chromosome execute(List<Chromosome> population){
        return null;
    }

    private List<Key<Node>> getListKeySortNodeByEarliestTime(List<Order> orders) {
        List<Key<Node>> keys = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            keys.add(Key.<Node>builder()
                .orderIndex(i)
                .type(PICKUP.name())
                .value(order.getPickup())
                .build());
            keys.add(Key.<Node>builder()
                .orderIndex(i)
                .type(DELIVERY.name())
                .value(order.getPickup())
                .build());
        }
        keys.sort(Comparator.comparing(Key::getValue));
        return keys;
    }

}
