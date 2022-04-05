package hcmut.thesis.gpduserver.controller.rest;

import hcmut.thesis.gpduserver.constants.enumations.BaseCodeEnum;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.UserSecure;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import hcmut.thesis.gpduserver.service.OrderService;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/orders")
public class OrderController {
@Autowired
  private OrderService orderService;

  @PostMapping("")
  public ApiResponse<Order> creatOrder(
      @RequestBody FormCreateOrder formCreateOrder,
      Authentication authentication ){
    UserSecure user = (UserSecure) authentication.getPrincipal();
    formCreateOrder.setUserId(user.getId());
    formCreateOrder.setUserName(user.getUsername());
    Order order = orderService.createOrder(formCreateOrder);
    if(Objects.isNull(order)){
      return new ApiResponse<Order>().fail(BaseCodeEnum.FAIL);
    }
    return new ApiResponse<Order>().success(order);
  }
  @GetMapping("/{orderId}")
  public ApiResponse<Order> getOrderById(
      @PathVariable String orderId,
      Authentication authentication ){
    UserSecure user = (UserSecure) authentication.getPrincipal();
    Order order = orderService.getOrderByIdAndUserId(orderId, user.getId());
    if(Objects.isNull(order)){
      return new ApiResponse<Order>().fail(BaseCodeEnum.FAIL);
    }
    return new ApiResponse<Order>().success(order);
  }

  @GetMapping("")
  public ApiResponse<List<Order>> getOrdersUser(
      @RequestParam(required = false, defaultValue = "") String status,
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false,defaultValue = "100") int limit,
      Authentication authentication ){
    UserSecure user = (UserSecure) authentication.getPrincipal();
    List<Order> orders = orderService.getOrdersUser(user.getId(), status, offset, limit);
    if(Objects.isNull(orders)){
      return new ApiResponse<List<Order>>().fail(BaseCodeEnum.FAIL);
    }
    return new ApiResponse<List<Order>>().success(orders);
  }


}
