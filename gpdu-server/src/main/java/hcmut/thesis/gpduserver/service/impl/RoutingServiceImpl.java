package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.ai.AIRouter;
import hcmut.thesis.gpduserver.ai.config.AIConfig;
import hcmut.thesis.gpduserver.ai.config.Cost;
import hcmut.thesis.gpduserver.ai.models.*;
import hcmut.thesis.gpduserver.ai.testcase.TestCaseConverter;
import hcmut.thesis.gpduserver.ai.utils.RoutingConverter;
import hcmut.thesis.gpduserver.config.cache.DynamicRoutingConfig;
import hcmut.thesis.gpduserver.config.cache.RoutingCache;
import hcmut.thesis.gpduserver.constants.enumations.TypeNode;
import hcmut.thesis.gpduserver.mapbox.MapboxClient;
import hcmut.thesis.gpduserver.models.entity.*;
import hcmut.thesis.gpduserver.repository.RoutingRepository;
import hcmut.thesis.gpduserver.service.OrderService;
import hcmut.thesis.gpduserver.service.RoutingService;
import hcmut.thesis.gpduserver.service.VehicleService;
import hcmut.thesis.gpduserver.utils.GsonUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoutingServiceImpl implements RoutingService {

    @Autowired
    private RoutingRepository routingRepository;
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
    private DynamicRoutingConfig dynamicRoutingConfig;





    @Override
    public List<Routing> createListRouting(List<Routing> requests) {
        List<Routing> routings = new ArrayList<>();
        try {
            closeAllActiveRouting();
            requests.forEach(request -> {
                routings.add(createRouting(request));
            });
        } catch (Exception e) {
            log.info("Error when createRouting, request: {}, exception: {}",
                    GsonUtils.toJsonString(requests), e.getMessage());
        }
        return routings;
    }

    private Routing createRouting(Routing request) {
        try {
            Optional<Routing> routing = routingRepository.insert(request);
            routing.ifPresent(value -> request.getNodes().forEach(nodeRouting ->
                    orderService.updateOrderRouting(nodeRouting.getOrderId(),
                            value.getId().toString())));
            log.info("createRouting, request: {}. result: {}", GsonUtils.toJsonString(request),
                    GsonUtils.toJsonString(routing.orElse(null)));
            return routing.orElse(null);
        } catch (Exception e) {
            log.info("Error when createRouting, request: {}, exception: {}",
                    GsonUtils.toJsonString(request), e.getMessage());
            return null;
        }
    }

    private void closeAllActiveRouting() {
        try {
            Document request = new Document().append("active", Boolean.TRUE);
            Document update = new Document().append("active", Boolean.FALSE);
            routingRepository.updateMany(request, update);
        } catch (Exception e) {
            log.info("Error when closeAllActiveRouting, exception: {}", e.getMessage());
        }
    }

    @Override
    public Routing getRoutingActiveByVehicleId(String vehicleId) {
        Document request = new Document().append("vehicleId", vehicleId).append("active", true);
        return getRoutingByRequest(request);
    }

    @Override
    public Routing getRoutingActiveByOrderId(String orderId) {
        Document request = new Document()
                .append("nodes.orderId", orderId)
                .append("active", true);
        return getRoutingByRequest(request);
    }

    @Override
    public Routing getRoutingByRequest(Document request) {
        try {
            Optional<Routing> routingOptional = routingRepository.getByQuery(request);
            log.info("getRoutingByRequest, request: {}, result: {}",
                    GsonUtils.toJsonString(request),
                    GsonUtils.toJsonString(routingOptional.orElse(null)));
            return routingOptional.orElse(null);
        } catch (Exception e) {
            log.info("Error when getOrderByRequest, request: {}, exception: {}",
                    GsonUtils.toJsonString(request),
                    e.getMessage());
            return null;
        }
    }

    @Override
    public List<Routing> getListRoutingByVehicle(String vehicleId, int offset, int limit) {
        try {
            Document request = new Document().append("vehicleId", vehicleId);
            Optional<List<Routing>> routingList = routingRepository.getMany(request, new Document(),
                    offset,
                    limit);
            log.info("getListRoutingByVehicleId: {}, offset: {}, limit: {},  result: {}",
                    vehicleId, offset, limit,
                    GsonUtils.toJsonString(routingList.orElse(new ArrayList<>())));
            return routingList.orElse(new ArrayList<>());
        } catch (Exception e) {
            log.error(
                    "Error when getListRoutingByVehicleId: {}, offset: {}, limit: {}, exception: {}",
                    vehicleId, offset, limit, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean updateActiveRouting(String routingId, Boolean active) {
        try {
            Optional<Routing> routingOptional = routingRepository.getById(routingId);
            if (routingOptional.isEmpty()) {
                log.info("UpdateActiveRouting routingId: {}, not found", routingId);
                return false;
            }
            Routing routing = routingOptional.get();
            routing.setActive(active);
            return updateRouting(routing);
        } catch (Exception e) {
            log.info("updateActiveRouting id: {}, exception: {}", routingId, e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updateRouting(Routing routing) {
        try {
            Optional<Boolean> result = routingRepository.update(routing.getId().toString(),
                    routing);
            log.info("updateRouting, routing: {}, result: {}", routing,
                    GsonUtils.toJsonString(result.orElse(false)));
            return result.orElse(false);
        } catch (Exception e) {
            log.info("Error updateRouting, routing: {}, exception: {}", routing, e.getMessage());
            return false;
        }
    }

    @Override
    public void routing() {
        List<Order> orders = orderService.getTodayOrders(System.currentTimeMillis());
        List<String> orderIds = orders.stream()
                .map(o -> o.getId().toHexString()).collect(Collectors.toList());
        List<Vehicle> vehicles = vehicleService.getVehicleList(0, 0);
        List<String> vehicleIds = vehicles.stream()
                .map(v -> v.getId().toHexString()).collect(Collectors.toList());
        List<RoutingVehicle> routingVehicles = new ArrayList<>();
        List<RoutingOrder> routingOrders = new ArrayList<>();
        List<Location> vehicleLocations = new ArrayList<>();
        List<Location> orderNodeLocations = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            Optional<Routing> routing = routingRepository.getByQuery(new Document()
                    .append("active", true)
                    .append("vehicleId", vehicle.getId().toHexString()));
            RoutingKey key = null;
            if (routing.isPresent()) {
                key = RoutingKey.builder()
                        .orderId(orderIds.indexOf(routing.get().getNextNode().getOrderId()))
                        .type(routing.get().getNextNode().getTypeNode())
                        .build();
            }
            RoutingVehicle routingVehicle = RoutingConverter.convertVehicle2RoutingVehicle(vehicle,
                    key);
            routingVehicles.add(routingVehicle);
            vehicleLocations.add(vehicle.getCurrentLocation());
        }
        for (Order order : orders) {
            RoutingOrder routingOrder = RoutingConverter.convertOrder2RoutingOrder(order,
                    vehicleIds.indexOf(order.getVehicleId()));
            routingOrders.add(routingOrder);
            orderNodeLocations.add(order.getPickup().getLocation());
            orderNodeLocations.add(order.getDelivery().getLocation());
        }

        AIConfig config = AIConfig.builder()
                .build();
        Storage storage = new Storage();
        Location repoLocation = storage.getLocation();
        RoutingMatrix routingMatrix = RoutingMatrix.builder()
                .orderNumber(orders.size())
                .vehicleNumber(vehicles.size())
                .orderNodeMatrix(mapboxClient.retrieveDurationMatrix(orderNodeLocations).orElseThrow())
                .vehicleMatrix(mapboxClient.retrieveDurationMatrix(vehicleLocations, orderNodeLocations)
                        .orElseThrow())
                .vehicleRepoList(mapboxClient.retrieveDurationMatrix(vehicleLocations, List.of(repoLocation)).orElseThrow()
                        .stream().flatMap(List::stream).collect(Collectors.toList()))
                .repoList(mapboxClient.retrieveDurationMatrix(orderNodeLocations, List.of(repoLocation)).orElseThrow()
                        .stream().flatMap(List::stream).collect(Collectors.toList()))
                .build();
        Cost cost = new Cost();
        List<Long> startTimeVehicle = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();
        for(int i = 0; i < vehicleIds.size(); i++){
            startTimeVehicle.add(currentTime);
        }
        AIRouter router = new AIRouter(routingOrders, routingVehicles, config, routingMatrix, repoLocation, cost, startTimeVehicle);
        RoutingResponse res = router.routing();
        this.saveRoutings(res, orders, vehicleIds);
    }


    @Override
    public RoutingResponse routing(InputStream inputStream) throws Exception {
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            int orderNumber = Integer.parseInt(br.readLine().split("\t")[1]);
            br.readLine();
            List<RoutingOrder> routingOrders = new ArrayList<>();
            for (int i = 0; i < orderNumber; i++) {
                RoutingOrder routingOrder = TestCaseConverter.convertString2RoutingOrder(
                        br.readLine());
                routingOrders.add(routingOrder);
            }
            br.readLine();
            List<List<Float>> orderNodeMatrix = new ArrayList<>();
            for (int i = 0; i < orderNumber * 2; i++) {
                orderNodeMatrix.add(Arrays.stream(br.readLine().split(","))
                        .map(Float::parseFloat).collect(Collectors.toList()));
            }
            long capacity = Long.parseLong(br.readLine().split("\t")[1]);
            int vehicleNumber = Integer.parseInt(br.readLine().split("\t")[1]);
            br.readLine();
            List<RoutingVehicle> routingVehicles = new ArrayList<>();
            for (int i = 0; i < vehicleNumber; i++) {
                RoutingVehicle routingVehicle = RoutingVehicle.builder()
                        .load(capacity)
                        .location(TestCaseConverter.convertString2VehicleLocation(br.readLine()))
                        .build();
                routingVehicles.add(routingVehicle);
            }
            br.readLine();
            List<List<Float>> vehicleMatrix = new ArrayList<>();
            for (int i = 0; i < vehicleNumber; i++) {
                vehicleMatrix.add(Arrays.stream(br.readLine().split(","))
                        .map(Float::parseFloat).collect(Collectors.toList()));
            }
            Location repoLocation = TestCaseConverter.convertString2RepoLocation(br.readLine());
            br.readLine();
            List<Float> repoDurationList = Arrays.stream(br.readLine().split(","))
                    .map(Float::parseFloat).collect(Collectors.toList());
            br.readLine();
            List<Float> vehicleRepoDurationList = Arrays.stream(br.readLine().split(","))
                    .map(Float::parseFloat).collect(Collectors.toList());
            AIConfig config = AIConfig.builder()
                    .startTime(0)
                    .build();
            RoutingMatrix routingMatrix = RoutingMatrix.builder()
                    .orderNumber(orderNumber)
                    .vehicleNumber(vehicleNumber)
                    .orderNodeMatrix(orderNodeMatrix)
                    .vehicleMatrix(vehicleMatrix)
                    .repoList(repoDurationList)
                    .vehicleRepoList(vehicleRepoDurationList)
                    .build();
            Cost cost = Cost.builder()
                    .travel(TestCaseConverter.convertString2Cost(br.readLine()))
                    .waiting(TestCaseConverter.convertString2Cost(br.readLine()))
                    .late(TestCaseConverter.convertString2Cost(br.readLine()))
                    .build();
            AIRouter router = new AIRouter(routingOrders, routingVehicles, config, routingMatrix, repoLocation, cost, null);
            return router.routing();
        }
    }

    @Override
    public List<Routing> routing(RoutingMatrix matrix, List<Order> orders) {

        return null;
    }


    private void saveRoutings(RoutingResponse routingResponse, List<Order> orders,
                              List<String> vehicleIds) {
        List<Routing> listRouting = buildRouting(routingResponse, orders, vehicleIds);
        this.createListRouting(listRouting);
    }

    private List<Routing> buildRouting(RoutingResponse routingResponse, List<Order> orders,
        List<String> vehicleIds){
        List<Routing> listRouting = new ArrayList<>();
        for (RoutingResponse.Route route : routingResponse.getRoutes()) {
            List<Routing.NodeRouting> nodeRoutings = new ArrayList<>();
            for (RoutingKey routingKey : route.getRoutingKeys()) {
                Order order = orders.get(routingKey.getOrderId());
                Node node = routingKey.getType().equals(TypeNode.PICKUP) ? order.getPickup()
                    : order.getDelivery();
                Routing.NodeRouting nodeRouting = Routing.NodeRouting.builder()
                    .address(node.getAddress())
                    .customerName(node.getCustomerName())
                    .earliestTime(node.getEarliestTime())
                    .latestTime(node.getLatestTime())
                    .location(node.getLocation())
                    .phone(node.getPhone())
                    .typeNode(routingKey.getType())
                    .orderId(order.getId().toHexString())
                    .build();
                nodeRoutings.add(nodeRouting);
            }
            Routing routing = Routing.builder()
                .vehicleId(vehicleIds.get(route.getVehicleId()))
                .nodes(nodeRoutings)
                .nextNode(nodeRoutings.get(0))
                .timeDoneNoes(route.getTimeDoneNode())
                .build();
            listRouting.add(routing);
        }
        return listRouting;
    }


}
