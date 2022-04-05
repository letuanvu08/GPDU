package hcmut.thesis.gpduserver.service;


import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import java.util.List;

public interface OrderService {
  Order createOrder(FormCreateOrder form);
  Order getOrderById(String id);
  Order getOrderByIdAndUserId(String orderId, String userId);
  List<Order> getOrdersUser(String userId, String status, int offset, int limit);
}
