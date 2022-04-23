package hcmut.thesis.gpduserver.controller.rest;

import hcmut.thesis.gpduserver.constants.enumations.BaseCodeEnum;
import hcmut.thesis.gpduserver.mapbox.IMapboxClient;
import hcmut.thesis.gpduserver.mapbox.commands.GetDurationCommand;
import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Order.Status;
import hcmut.thesis.gpduserver.models.entity.UserSecure;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import hcmut.thesis.gpduserver.models.request.order.FormCreateOrder;
import hcmut.thesis.gpduserver.models.request.order.GenerateRandomRequest;
import hcmut.thesis.gpduserver.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

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
    @Autowired
    private IMapboxClient mapboxClient;

    @GetMapping("/test")
    public List<List<Float>> test() {
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            Location location = new Location();
            location.setLatitude((float) ThreadLocalRandom.current().nextDouble(10.653092, 10.957113));
            location.setLongitude((float) ThreadLocalRandom.current().nextDouble(106.471501, 106.702647));
            locations.add(location);
        }


        return mapboxClient.retrieveDurationMatrix(locations).orElse(new ArrayList<>());
    }

    @PostMapping("")
    public ApiResponse<Order> creatOrder(
            @RequestBody FormCreateOrder formCreateOrder,
            Authentication authentication) {
        UserSecure user = (UserSecure) authentication.getPrincipal();
        formCreateOrder.setUserId(user.getId());
        formCreateOrder.setUserName(user.getUsername());
        Order order = orderService.createOrder(formCreateOrder);
        if (Objects.isNull(order)) {
            return new ApiResponse<Order>().fail(BaseCodeEnum.FAIL);
        }
        return new ApiResponse<Order>().success(order);
    }

    @PostMapping("/generate-random")
    public ApiResponse<Boolean> generateRandomOrders(
            @RequestBody GenerateRandomRequest request) {
        orderService.generateRandomOrders(request);
        return new ApiResponse<Boolean>().success(true);
    }

    @GetMapping("/{orderId}")
    public ApiResponse<Order> getOrderById(
            @PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (Objects.isNull(order)) {
            return new ApiResponse<Order>().fail(BaseCodeEnum.FAIL);
        }
        return new ApiResponse<Order>().success(order);
    }

    @GetMapping("")
    public ApiResponse<List<Order>> getOrdersUser(
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "100") int limit,
            Authentication authentication) {
        UserSecure user = (UserSecure) authentication.getPrincipal();
        List<Order> orders = orderService.getOrderBysUserId(user.getId(), status, offset, limit);
        if (Objects.isNull(orders)) {
            return new ApiResponse<List<Order>>().fail(BaseCodeEnum.FAIL);
        }
        return new ApiResponse<List<Order>>().success(orders);
    }

    @GetMapping("/vehicels/{vehicleId}")
    public ApiResponse<List<Order>> getOrderByVehicleId(
            @PathVariable String vehicleId,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "100") int limit) {
        List<Order> orders = orderService.getOrdersByVehicleId(vehicleId, status, offset, limit);
        if (Objects.isNull(orders)) {
            return new ApiResponse<List<Order>>().fail(BaseCodeEnum.FAIL);
        }
        return new ApiResponse<List<Order>>().success(orders);
    }

    @PostMapping("/{orderId}/status")
    public ApiResponse<Boolean> updateStatusOrder(
            @PathVariable String orderId,
            @RequestBody Status status) {
        Boolean result = orderService.updateOrderStatus(orderId, status);
        return new ApiResponse<Boolean>().success(result);
    }


}
