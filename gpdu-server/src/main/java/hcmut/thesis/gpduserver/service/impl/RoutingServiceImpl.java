package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.ai.AIRouter;
import hcmut.thesis.gpduserver.ai.config.AIConfig;
import hcmut.thesis.gpduserver.ai.models.*;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.constants.enumations.TypeNode;
import hcmut.thesis.gpduserver.constants.mongodb.QueryOperators;
import hcmut.thesis.gpduserver.mapbox.MapboxClient;
import hcmut.thesis.gpduserver.models.entity.*;
import hcmut.thesis.gpduserver.models.request.order.OrderListRequest;
import hcmut.thesis.gpduserver.models.request.routing.RequestCreateRouting;
import hcmut.thesis.gpduserver.repository.RoutingRepository;
import hcmut.thesis.gpduserver.service.OrderService;
import hcmut.thesis.gpduserver.service.RoutingService;
import hcmut.thesis.gpduserver.service.VehicleService;
import hcmut.thesis.gpduserver.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import hcmut.thesis.gpduserver.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;

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
                    orderService.updateOrderRouting(nodeRouting.getOrderId(), value.getId().toString())));
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
                .append("listOrderId", new Document().append(QueryOperators.ALL, List.of(orderId)))
                .append("active", true);
        return getRoutingByRequest(request);
    }

    @Override
    public Routing getRoutingByRequest(Document request) {
        try {
            Optional<Routing> routingOptional = routingRepository.getByQuery(request);
            log.info("getRoutingByRequest, request: {}, result: {}",
                    GsonUtils.toJsonString(request), GsonUtils.toJsonString(routingOptional.orElse(null)));
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
                    vehicleId, offset, limit, GsonUtils.toJsonString(routingList.orElse(new ArrayList<>())));
            return routingList.orElse(new ArrayList<>());
        } catch (Exception e) {
            log.error("Error when getListRoutingByVehicleId: {}, offset: {}, limit: {}, exception: {}",
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
            Optional<Boolean> result = routingRepository.update(routing.getId().toString(), routing);
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
        long startTime = System.currentTimeMillis() + TimeUtils.ONE_HOUR_IN_MILLIS_SECOND;
        List<Order> orders = orderService.getTodayOrders(startTime);
        List<String> orderIds = orders.stream()
                .map(o -> o.getId().toHexString()).collect(Collectors.toList());
        List<Vehicle> vehicles = vehicleService.getVehicleList(0, 0);
        List<String> vehicleIds = vehicles.stream()
                .map(v -> v.getId().toHexString()).collect(Collectors.toList());
        List<RoutingOrder> routingOrders = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<RoutingVehicle> routingVehicles = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            Optional<Routing> routing = routingRepository.getByQuery(new Document()
                    .append("active", true)
                    .append("vehicleId", vehicles.get(i).getId().toHexString()));
            RoutingVehicle routingVehicle = RoutingVehicle.builder()
                    .id(i)
                    .location(vehicles.get(i).getCurrentLocation())
                    .build();
            routing.ifPresent(value -> routingVehicle.setNextNode(RoutingKey.builder()
                    .orderId(orderIds.indexOf(value.getNextNode().getOrderId()))
                    .type(value.getNextNode().getTypeNode())
                    .build()));
            routingVehicles.add(routingVehicle);
            locations.add(vehicles.get(i).getCurrentLocation());
        }
        for (int i = 0; i < orders.size(); i++) {
            RoutingOrder routingOrder = RoutingOrder.builder()
                    .id(i)
                    .delivery(RoutingOrder.RoutingNode.builder()
                            .earliestTime(orders.get(i).getDelivery().getEarliestTime())
                            .latestTime(orders.get(i).getDelivery().getLatestTime())
                            .location(orders.get(i).getDelivery().getLocation())
                            .build())
                    .pickup(RoutingOrder.RoutingNode.builder()
                            .earliestTime(orders.get(i).getPickup().getEarliestTime())
                            .latestTime(orders.get(i).getPickup().getLatestTime())
                            .build())
                    .vehicleId(vehicleIds.indexOf(orders.get(i).getVehicleId()))
                    .vehicleConstant(!orders.get(i).getCurrentStep().getStep().equals(StepOrderEnum.ORDER_RECEIVED.getLabel()))
                    .build();
            routingOrders.add(routingOrder);
            locations.add(orders.get(i).getPickup().getLocation());
            locations.add(orders.get(i).getDelivery().getLocation());
        }
        AIConfig config = AIConfig.builder()
                .elitismRate(0.05f)
                .lateCost(0.2f)
                .waitingCost(0.1f)
                .travelCost(2f)
                .populationSize(50)
                .tournamentSize(5)
                .maxGeneration(100)
                .crossover(0.8f)
                .mutation(0.2f)
                .startTime(startTime)
                .build();
        RoutingMatrix routingMatrix = RoutingMatrix.builder()
                .orderNumber(orders.size())
                .vehicleNumber(vehicles.size())
                .value(mapboxClient.retrieveDurationMatrix(locations).orElseThrow())
                .build();
        AIRouter router = new AIRouter(routingOrders, routingVehicles, config, routingMatrix);
        RoutingResponse res = router.routing();
        List<Routing> listRouting = new ArrayList<>();
        for (RoutingResponse.Route route : res.getRoutes()) {
            List<Routing.NodeRouting> nodeRoutings = new ArrayList<>();
            for (RoutingKey routingKey : route.getRoutingKeys()) {
                Order order = orders.get(routingKey.getOrderId());
                Node node = routingKey.getType().equals(TypeNode.PICKUP) ? order.getPickup() : order.getDelivery();
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
                    .build();
            listRouting.add(routing);

        }
        createListRouting(listRouting);
    }
}
