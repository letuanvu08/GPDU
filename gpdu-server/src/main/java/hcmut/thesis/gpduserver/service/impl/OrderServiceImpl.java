package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.constants.enumations.StatusOrderEnum;
import hcmut.thesis.gpduserver.constants.enumations.StepOrderEnum;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Order.Status;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import hcmut.thesis.gpduserver.repository.OrderRepository;
import hcmut.thesis.gpduserver.service.OrderService;
import hcmut.thesis.gpduserver.utils.GsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository orderRepository;

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
      log.info("createOrder form: {}, result: {}", form, order);
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
      log.error(" getOrderById: {}, resultL: {}", id, orderOptional.orElse(null));
      return orderOptional.orElse(null);
    } catch (Exception e) {
      log.error("Error when getOrderById: {}", e.getMessage());
      return null;
    }
  }

  @Override
  public List<Order> getOrderBysUserId(String userId, String status, int offset, int limit) {
    try {
      Document request = new Document().append("userId", userId);
      if (!Strings.isBlank(status)) {
        request.append("currentStatus", status);
      }
      Optional<List<Order>> orders = this.orderRepository.getMany(request, new Document(), offset,
          limit);
      log.info("getOrdersUser by userId: {}, status: {}, offset: {}, limit: {}, result: {}", userId,
          status, offset, limit,
          GsonUtils.toJsonString(orders.orElse(new ArrayList<>())));
      return orders.orElse(new ArrayList<>());
    } catch (Exception e) {
      log.error("getOrdersUser by userId: {}, status: {}, offset: {}, limit: {}, error: {}", userId,
          status, offset, limit, e.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public List<Order> getOrdersByVehicleId(String vehicleId, String status, int offset, int limit) {
    try {
      Document request = new Document().append("vehicleId", vehicleId);
      if (!Strings.isBlank(status)) {
        request.append("currentStatus", status);
      }
      Optional<List<Order>> orders = this.orderRepository.getMany(request, new Document(), offset,
          limit);
      log.info(
          "getOrdersByVehicleId by vehicleId: {}, status: {}, offset: {}, limit: {}, result: {}",
          vehicleId,
          status, offset, limit,
          GsonUtils.toJsonString(orders.orElse(new ArrayList<>())));
      return orders.orElse(new ArrayList<>());
    } catch (Exception e) {
      log.error(
          "getOrdersByVehicleId by vehicleId: {}, status: {}, offset: {}, limit: {}, error: {}",
          vehicleId,
          status, offset, limit, e.getMessage());
      return new ArrayList<>();
    }
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
      Optional<Boolean> result = orderRepository.update(orderId, order);
      log.info("assignOrderForVehicle orderId: {}, result: {}", order, result.orElse(false));
      return result.orElse(false);
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
      Optional<Boolean> result = orderRepository.update(orderId, order);
      log.info("updateOrderStatus orderId: {}, result: {}", order, result.orElse(false));
      return result.orElse(false);
    } catch (Exception e) {
      log.error("Error when updateOrderStatus: {}", e.getMessage());
      return false;
    }
  }
}