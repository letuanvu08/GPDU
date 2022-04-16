package hcmut.thesis.gpduserver.ai;

import hcmut.thesis.gpduserver.ai.models.Chromosome;
import hcmut.thesis.gpduserver.ai.models.Key;
import hcmut.thesis.gpduserver.ai.utils.RandomKey;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.mapbox.IMapboxClient;
import hcmut.thesis.gpduserver.mapbox.commands.GetDurationCommand;
import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.service.OrderService;
import hcmut.thesis.gpduserver.service.VehicleService;
import hcmut.thesis.gpduserver.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AIRouter implements IAIRouter {
    @Autowired
    private OrderService orderService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private IMapboxClient mapboxClient;

    private final int POPULATION_SIZE = 50;
    private final int GENERATION_MAX = 50;
    private final String PICKUP = "pickup";
    private final String DELIVERY = "delivery";
    private final Integer TRAVEL_COST = 200;
    private final Integer WAITING_COST = 10;
    private final Integer LATE_COST = 20;
    private List<Order> orders = new ArrayList<>();

    private List<Chromosome.Gene> getSample(List<String> vehicleIds) {
        List<Chromosome.Gene> sample = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            Chromosome.Gene gene = null;
            if (!StepOrderEnum.ORDER_RECEIVED.name().equals(order.getCurrentStep().getStatus())) {
                int index = vehicleIds.indexOf(order.getVehicleId());
                int pickup = RandomKey.generate(index);
                int delivery = RandomKey.generate(index, pickup);
                gene = Chromosome.Gene.builder()
                        .pickup(pickup)
                        .delivery(delivery)
                        .build();
            }
            sample.add(gene);
        }
        return sample;
    }

    private Location getCoordinates(Key key) {
        Location location;
        Order order = orders.get(key.getOrderIndex());
        if (key.getType().equals(PICKUP)) {
            location = order.getPickup().getLocation();
        } else {
            location = order.getDelivery().getLocation();
        }
        return location;
    }

    private float calRouteDuration(List<Location> route) {
        float duration = 0;
        long timestamp = System.currentTimeMillis() / 1000 + 60 * 30;
        for (int i = 0; i < route.size() - 1; i++) {
            GetDurationCommand command = GetDurationCommand.builder()
                    .fromLocation(route.get(i))
                    .toLocation(route.get(i + 1))
                    .departAt(TimeUtils.convertTimestampToUTC(timestamp))
                    .build();
            duration += mapboxClient.getDuration(command);
        }
        return duration;
    }

    private float calTotalDuration(List<Key> keys) {
        List<Location> locations = new ArrayList<>();
        List<List<Location>> routes = new ArrayList<>();
        int vehicle = 0;
        for (Key key : keys) {
            int temp = key.getValue() / 1000;
            if (temp != vehicle) {
                vehicle = temp;
                routes.add(locations);
                locations = new ArrayList<>();
                locations.add(this.getCoordinates(key));
            } else {
                locations.add(this.getCoordinates(key));
            }
        }
        float duration = 0;
        for (List<Location> route : routes) {
            duration += this.calRouteDuration(route);
        }
        return duration;
    }

    private float calFitness(List<Chromosome.Gene> genes) {
        List<Key> keys = new ArrayList<>();
        for (int i = 0; i < genes.size(); i++) {
            keys.add(Key.builder()
                    .value(genes.get(i).getPickup())
                    .orderIndex(i)
                    .type(PICKUP)
                    .build());
            keys.add(Key.builder()
                    .value(genes.get(i).getDelivery())
                    .orderIndex(i)
                    .type(DELIVERY)
                    .build());
        }
        keys.sort(Comparator.comparingInt(Key::getValue));
        float duration = this.calTotalDuration(keys);
        return duration * TRAVEL_COST;
    }

    private List<Chromosome> initPopulation(List<String> vehicleIds) {
        List<Chromosome.Gene> sample = this.getSample(vehicleIds);
        List<Chromosome> result = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Chromosome.Gene> genes = sample.stream().map(item -> {
                if (item == null) {
                    int pickup = RandomKey.generateVehicleRandom(vehicleIds.size());
                    int delivery = RandomKey.generateVehicleRandom(vehicleIds.size(), pickup);
                    return Chromosome.Gene.builder()
                            .pickup(pickup)
                            .delivery(delivery)
                            .build();
                }
                return item;
            }).collect(Collectors.toList());
            Chromosome chromosome = Chromosome.builder()
                    .genes(genes)
                    .fitness(this.calFitness(genes))
                    .build();
            result.add(chromosome);
        }
        for (Chromosome chromosome : result) {
            chromosome.setFitness(this.calFitness(chromosome.getGenes()));
        }
        result.sort(((c1, c2) -> (int) Math.ceil(c1.getFitness() - c2.getFitness()) + 1));
        return result;
    }

    public Chromosome mate(Chromosome c1, Chromosome c2) {
        return null;
    }

    public Chromosome mutate() {
        return null;
    }

    @Override
    public void routing() {
        this.orders = orderService.getTodayOrders();
        List<String> vehicleIds = vehicleService.getVehicleList(0, 0).stream().map(v -> v.getId().toHexString())
                .collect(Collectors.toList());
        List<Chromosome> population = this.initPopulation(vehicleIds);


    }

}
