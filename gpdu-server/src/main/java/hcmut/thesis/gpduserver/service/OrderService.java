package hcmut.thesis.gpduserver.service;


import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Order.Status;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import hcmut.thesis.gpduserver.models.request.order.GenerateRandomRequest;
import hcmut.thesis.gpduserver.models.request.order.OrderListRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(FormCreateOrder form);

    Order getOrderById(String id);

    List<Order> getOrderListByRequest(OrderListRequest request);

    List<Order> getOrderBysUserId(String userId, String status, int offset, int limit);

    List<Order> getOrdersByVehicleId(String vehicleId, String status, int offset, int limit);

    Boolean assignOrderForVehicle(String orderId, String vehicleId);

    Boolean updateOrderStatus(String orderId, Status status);

    Boolean updateOrderRouting(String orderId, String currentRoutingId);

    Boolean updateOrder(Order order);

    void updateCurrentLocationByVehicleId(String vehicleId, Location location);

    List<Order> getTodayOrders(long start);

    void generateRandomOrders(GenerateRandomRequest request);

}
