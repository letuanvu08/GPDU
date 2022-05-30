package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.ai.models.RoutingMatrix;
import hcmut.thesis.gpduserver.config.cache.DynamicRoutingConfig;
import hcmut.thesis.gpduserver.config.cache.RoutingCache;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.mapbox.MapboxClient;
import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Node;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Order.Status;
import hcmut.thesis.gpduserver.models.entity.Storage;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import hcmut.thesis.gpduserver.models.request.order.GenerateRandomRequest;
import hcmut.thesis.gpduserver.utils.LocationUtils;
import hcmut.thesis.gpduserver.utils.TimeUtils;
import hcmut.thesis.gpduserver.utils.duration.DurationCalculator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DynamicRouting {

    @Autowired
    private OrderService orderService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private MapboxClient mapboxClient;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private RoutingCache routingCache;

    @Autowired
    private RoutingService routing;

    @Autowired
    private DynamicRoutingConfig dynamicRoutingConfig;

    @Scheduled(cron = "*/12 * * * * *")
    public void autoTriggerDynamicRouting() {
        log.info("check autoTriggerDynamicRouting");
        if (isOverThreshold()) {
            log.info("autoTriggerDynamicRouting");
            routingCache.cleanCache();
            /// m thích trigger cái gi thì tuỳ , có thể là dùng test api để lấy order
            // hoặc load order dưới database
            List<Order> orders = routingCache.getOrdersCache();
            RoutingMatrix matrix = getRoutingMatrix(orders);
            routing.routing(matrix, orders);
        }
    }

    private RoutingMatrix getRoutingMatrix(List<Order> orders) {
        Storage storage = Storage.builder().build();
        List<List<Float>> orderDuration = DurationCalculator.getOrderDurationMatrix(orders);
        List<List<Float>> vehicleDuration = DurationCalculator.getVehicleDurationMatrix(
            routingCache.getVehicles(), orders);
        List<Float> vehicleDepot = DurationCalculator.getVehicleRepoDurationList(
            storage.getLocation(), routingCache.getVehicles());
        List<Float> orderDepot = DurationCalculator.getRepoDurationList(storage.getLocation(),
            orders);
        return RoutingMatrix.builder()
            .orderNumber(orders.size())
            .vehicleNumber(routingCache.getVehicles().size())
            .orderNodeMatrix(orderDuration)
            .vehicleMatrix(vehicleDuration)
            .repoList(orderDepot)
            .vehicleRepoList(vehicleDepot)
            .build();
    }

    @Scheduled(cron = "*/12 * * * * *")
    public void autoCreateOrder() {
        log.info("autoCreateOrder");
        generateRandomOrders(GenerateRandomRequest.builder().number(1).build());
    }

    @Scheduled(cron = "*/12 * * * * *")
    public void autoUpdateStatusOrders() {
        log.info("autoUpdateStatusOrders");
        Long current = System.currentTimeMillis();
        routingCache.getRoutes().stream()
            .forEach(routing -> {
                for (int i = 0; i < routing.getTimeDoneNoes().size(); i++) {
                    Float timeDoneNode = routing.getTimeDoneNoes().get(i);
                    if (timeDoneNode < current) {
                        String id = routing.getNodes().get(i).getOrderId();
                        Optional<Order> orderOptional = routingCache.getOrdersCache().stream()
                            .filter(order -> order.getId().toString().equals(id))
                            .findFirst();
                        orderOptional.ifPresent(
                            order -> updateStatusOrderAndVehicle(order, routing.getVehicleId()));
                    }
                }
            });
    }

    private void updateStatusOrderAndVehicle(Order order, String vehicleId) {
        if (Objects.equals(order.getCurrentStep().getStep(),
            StepOrderEnum.ORDER_RECEIVED.getLabel())) {
            order.setCurrentStep(
                Status.builder().step(StepOrderEnum.PICKUP_PACKAGE.getLabel()).build());
            Optional<Vehicle> vehicleOptional = routingCache.getVehicles().stream()
                .filter(vehicle -> vehicle.getId().toString().equals(vehicleId))
                .findFirst();
            if (vehicleOptional.isPresent()) {
                vehicleOptional.get().setCurrentLocation(order.getPickup().getLocation());
            }
        } else if (Objects.equals(order.getCurrentStep().getStep(),
            StepOrderEnum.PICKUP_PACKAGE.getLabel())) {
            order.setCurrentStep(Status.builder().step(StepOrderEnum.DELIVERED.getLabel()).build());
            Optional<Vehicle> vehicleOptional = routingCache.getVehicles().stream()
                .filter(vehicle -> vehicle.getId().toString().equals(vehicleId))
                .findFirst();
            if (vehicleOptional.isPresent()) {
                vehicleOptional.get().setCurrentLocation(order.getPickup().getLocation());
            }
            routingCache.getOrdersCache().remove(order);
        }

    }

    private boolean isOverThreshold() {
        Long currentTimestamp = System.currentTimeMillis();
        float point = routingCache.getOrdersCache()
            .stream()
            .map(order -> pointOfOrder(currentTimestamp, order.getPickup().getEarliestTime()))
            .reduce(0f, Float::sum);
        return point >= dynamicRoutingConfig.getThreshold();
    }

    private float pointOfOrder(Long currentTimestamp, Long orderTimestamp) {
        return dynamicRoutingConfig.getWeightTime() / ((orderTimestamp - currentTimestamp));
    }

    public void generateRandomOrders(GenerateRandomRequest request) {
        for (int i = 0; i < request.getNumber(); i++) {
            long current = System.currentTimeMillis();
            long startTime = TimeUtils.generateRandomTimeInToday(
                current + TimeUtils.ONE_HOUR_IN_MILLIS_SECOND,
                current + 2 * TimeUtils.ONE_HOUR_IN_MILLIS_SECOND);
            Location location = LocationUtils.generateHCMUTLocation();
            Node pickup = Node.builder()
                .location(location)
                .earliestTime(startTime)
                .latestTime(TimeUtils.generateRandomTimeInToday(startTime,
                    startTime + TimeUtils.ONE_HOUR_IN_MILLIS_SECOND))
//                .address(mapboxClient.reverseGeocoding(location))
                .customerName("tu")
                .phone("0832183021")
                .build();
            startTime = TimeUtils.generateRandomTimeInToday(
                startTime + TimeUtils.ONE_HOUR_IN_MILLIS_SECOND,
                startTime + 2 * TimeUtils.ONE_HOUR_IN_MILLIS_SECOND);
            location = LocationUtils.generateHCMUTLocation();
            Node delivery = Node.builder()
                .location(location)
                .earliestTime(startTime)
                .latestTime(TimeUtils.generateRandomTimeInToday(startTime,
                    startTime + TimeUtils.ONE_HOUR_IN_MILLIS_SECOND))
                .address(mapboxClient.reverseGeocoding(location))
                .customerName("vu")
                .phone("0908978878")
                .build();
            Order.Package packageInfo = Order.Package.builder()
                .weight(ThreadLocalRandom.current().nextLong(1, 10))
                .name("package")
                .category("secret")
                .build();
            FormCreateOrder formCreateOrder = FormCreateOrder.builder()
                .pickup(pickup)
                .delivery(delivery)
                .packageInfo(packageInfo)
                .build();
            orderService.createOrder(formCreateOrder);
        }
    }

}
