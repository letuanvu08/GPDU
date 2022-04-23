package hcmut.thesis.gpduserver.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import hcmut.thesis.gpduserver.constants.enumations.StatusOrderEnum;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.mapbox.IMapboxClient;
import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Node;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Order.Status;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import hcmut.thesis.gpduserver.models.request.order.GenerateRandomRequest;
import hcmut.thesis.gpduserver.models.request.order.OrderListRequest;
import hcmut.thesis.gpduserver.repository.OrderRepository;
import hcmut.thesis.gpduserver.service.OrderService;
import hcmut.thesis.gpduserver.utils.GsonUtils;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import hcmut.thesis.gpduserver.utils.LocationUtils;
import hcmut.thesis.gpduserver.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private IMapboxClient mapboxClient;

    @Override
    public Order createOrder(FormCreateOrder form) {
        Order order = null;
        try {
            Status status = Status.builder()
                    .step(StepOrderEnum.ORDER_RECEIVED.getLabel())
                    .status(StatusOrderEnum.FINISHED.name())
                    .timestamp(System.currentTimeMillis())
                    .build();
            Order orderRequest = Order.builder()
                    .userId(form.getUserId())
                    .userName(form.getUserName())
                    .currentStep(status)
                    .delivery(form.getDelivery())
                    .pickup(form.getPickup())
                    .currentLocation(form.getPickup().getLocation())
                    .historyStatus(List.of(status))
                    .packageInfo(form.getPackageInfo())
                    .currentStatus(StatusOrderEnum.UNFINISHED.name())
                    .note(form.getNote())
                    .build();
            order = orderRepository.insert(orderRequest).orElse(null);
            log.info("createOrder form: {}, result: {}", form, GsonUtils.toJsonString(order));
            return order;
        } catch (Exception e) {
            log.error("Error when createOrder: {}", e.getMessage());
            return order;
        }
    }


    @Override
    public Order getOrderById(String id) {
        try {
            Optional<Order> orderOptional = this.orderRepository.getById(id);
            log.info(" getOrderById: {}, resultL: {}", id,
                    GsonUtils.toJsonString(orderOptional.orElse(null)));
            return orderOptional.orElse(null);
        } catch (Exception e) {
            log.error("Error when getOrderById: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Order> getOrderListByRequest(OrderListRequest orderListRequest) {
        try {
            Document request = new Document();
            if (!Strings.isEmpty(orderListRequest.getUserId())) {
                request.append("userId", orderListRequest.getUserId());
            }
            if (!Strings.isEmpty(orderListRequest.getVehicleId())) {
                request.append("vehicleId", orderListRequest.getVehicleId());
            }
            if (!Strings.isEmpty(orderListRequest.getLabelStep())) {
                request.append("currentStep", new Document("step", orderListRequest.getLabelStep()));
            }
            if (!Strings.isEmpty(orderListRequest.getStatus())) {
                request.append("currentStatus", orderListRequest.getStatus());
            }
            int offset = orderListRequest.getOffset();
            int limit = orderListRequest.getLimit();
            Optional<List<Order>> orders = this.orderRepository.getMany(request,
                    new Document("createdTime", -1), offset,
                    limit);
            log.info("getOrdersUser by request: {}, offset: {}, limit: {}, result: {}",
                    GsonUtils.toJsonString(request), offset, limit,
                    GsonUtils.toJsonString(orders.orElse(new ArrayList<>())));
            return orders.orElse(new ArrayList<>());
        } catch (Exception e) {
            log.error("getOrdersUser by request: {}, error: {}", orderListRequest, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Order> getOrderBysUserId(String userId, String status, int offset, int limit) {
        OrderListRequest request = OrderListRequest.builder()
                .userId(userId)
                .status(status)
                .offset(offset)
                .limit(limit)
                .build();
        return getOrderListByRequest(request);
    }

    @Override
    public List<Order> getOrdersByVehicleId(String vehicleId, String status, int offset, int limit) {
        OrderListRequest request = OrderListRequest.builder()
                .vehicleId(vehicleId)
                .status(status)
                .offset(offset)
                .limit(limit)
                .build();
        return getOrderListByRequest(request);
    }


    @Override
    public Boolean assignOrderForVehicle(String orderId, String vehicleId) {
        try {
            Optional<Order> orderOptional = this.orderRepository.getById(orderId);
            if (orderOptional.isEmpty()) {
                log.info("assignOrderForVehicle orderId: {} not found", orderOptional);
                return false;
            }
            Order order = orderOptional.get();
            order.setVehicleId(vehicleId);
            return updateOrder(order);
        } catch (Exception e) {
            log.error("Error when assignOrderForVehicle: {}", e.getMessage());
            return false;
        }
    }


    @Override
    public Boolean updateOrderStatus(String orderId, Status status) {
        try {
            Optional<Order> orderOptional = this.orderRepository.getById(orderId);
            if (orderOptional.isEmpty()) {
                log.info("updateOrderStatus orderId: {} not found", orderOptional);
                return false;
            }
            Order order = orderOptional.get();
            order.getHistoryStatus().add(status);
            if (StepOrderEnum.Done.getLabel().equals(status.getStep()) ||
                    StatusOrderEnum.CANCEL.name().equals(status.getStatus())) {
                order.setCurrentStatus(status.getStatus());
            }
            order.setCurrentStatus(status.getStatus());
            order.setCurrentStep(status);
            return updateOrder(order);
        } catch (Exception e) {
            log.error("Error when updateOrderStatus: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Order> getTodayOrders(long start) {
        Document query = new Document()
                .append("currentStatus", StatusOrderEnum.UNFINISHED.toString())
                .append("$or", Arrays.asList(
                        new Document("$and", Arrays.asList(
                                new Document("pickup.earliestTime", new Document("$gte", start)),
                                new Document("pickup.earliestTime", new Document("$lte", TimeUtils.atEndOfDay())
                                ))),
                        new Document("$and", Arrays.asList(
                                new Document("delivery.earliestTime", new Document("$gte", start)),
                                new Document("delivery.earliestTime", new Document("$lte", TimeUtils.atEndOfDay()))
                        ))));
        return orderRepository.getMany(query, new Document(), 0, 0).orElse(new ArrayList<>());
    }

    @Override
    public void generateRandomOrders(GenerateRandomRequest request) {
        for (int i = 0; i < request.getNumber(); i++) {
            long startTime = TimeUtils.generateRandomTimeInToday(System.currentTimeMillis());
            Location location = LocationUtils.generateHCMUTLocation();
            Node pickup = Node.builder()
                    .location(location)
                    .earliestTime(startTime)
                    .latestTime(startTime + ThreadLocalRandom.current().nextLong(TimeUtils.ONE_HOUR_IN_MILLIS_SECOND,
                            2 * TimeUtils.ONE_HOUR_IN_MILLIS_SECOND))
                    .address(mapboxClient.reverseGeocoding(location))
                    .customerName("tu")
                    .phone("0832183021")
                    .build();
            startTime = TimeUtils.generateRandomTimeInToday(System.currentTimeMillis());
            location = LocationUtils.generateHCMUTLocation();
            Node delivery = Node.builder()
                    .location(location)
                    .earliestTime(startTime)
                    .latestTime(startTime + ThreadLocalRandom.current().nextLong(TimeUtils.ONE_HOUR_IN_MILLIS_SECOND,
                            2 * TimeUtils.ONE_HOUR_IN_MILLIS_SECOND))
                    .address(mapboxClient.reverseGeocoding(location))
                    .customerName("vu")
                    .phone("0908978878")
                    .build();
            FormCreateOrder formCreateOrder = FormCreateOrder.builder()
                    .pickup(pickup)
                    .delivery(delivery)
                    .build();
            this.createOrder(formCreateOrder);
        }
    }

    @Override
    public Boolean updateOrderRouting(String orderId, String currentRoutingId) {
        try {
            Optional<Order> orderOptional = this.orderRepository.getById(orderId);
            if (orderOptional.isEmpty()) {
                log.info("assignOrderForVehicle orderId: {} not found", orderOptional);
                return false;
            }
            Order order = orderOptional.get();
            order.setCurrentRoutingId(currentRoutingId);
            return updateOrder(order);
        } catch (Exception e) {
            log.error("Error when assignOrderForVehicle: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updateOrder(Order order) {
        try {
            Optional<Boolean> result = orderRepository.update(order.getId().toString(), order);
            log.info("updateOrder, order: {}, result: {}", order, result.orElse(false));
            return result.orElse(false);
        } catch (Exception e) {
            log.error("Error when updateOrder: {}, exception: {}", order, e.getMessage());
            return false;
        }
    }
}