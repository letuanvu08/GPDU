package hcmut.thesis.gpduserver.service;


import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Order.Status;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import java.util.List;

public interface OrderService {
  Order createOrder(FormCreateOrder form);
  Order getOrderById(String id);
  List<Order> getOrderBysUserId(String userId, String status, int offset, int limit);
  List<Order> getOrdersByVehicleId(String vehicleId, String status, int offset, int limit);
  Boolean assignOrderForVehicle(String orderId, String vehicleId);
  Boolean updateOrderStatus(String orderId, Status status);

}
