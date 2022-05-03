package hcmut.thesis.gpduserver.controller.rest;

import com.google.protobuf.Api;
import hcmut.thesis.gpduserver.ai.models.RoutingResponse;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import hcmut.thesis.gpduserver.models.request.order.OrderListRequest;
import hcmut.thesis.gpduserver.models.request.routing.RequestCreateRouting;
import hcmut.thesis.gpduserver.models.request.routing.RequestGetRouting;
import hcmut.thesis.gpduserver.models.request.vehicle.FormAddVehicle;
import hcmut.thesis.gpduserver.service.*;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoutingService routingService;


    @GetMapping("/orders")
    public ApiResponse<List<Order>> getListOrders(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "false") boolean justActive
    ) {
        List<Order> orders = orderService.getOrderListByRequest(
                OrderListRequest.builder()
                        .offset(offset)
                        .limit(limit)

                        .build());
        return new ApiResponse<List<Order>>().success(orders);
    }

    @GetMapping("/routing/orders/{orderId}")
    public ApiResponse<Routing> getRoutingByOrder(@PathVariable String orderId) {
        Routing routing = routingService.getRoutingActiveByOrderId(orderId);
        return new ApiResponse<Routing>().success(routing);
    }

    @GetMapping("/routing/vehicles/{vehicleId}")
    public ApiResponse<Routing> getRoutingByVehicle(@PathVariable String vehicleId) {
        Routing routing = routingService.getRoutingActiveByVehicleId(vehicleId);
        return new ApiResponse<Routing>().success(routing);
    }

    @PostMapping("/vehicles")
    public ApiResponse<Vehicle> addVehicle(@RequestBody FormAddVehicle formAddVehicle) {
        Vehicle vehicle = vehicleService.addVehicle(formAddVehicle);
        return new ApiResponse<Vehicle>().success(vehicle);
    }

    //   cái này dùng để test trong khi chưa có AI
    @PostMapping("/routing")
    public ApiResponse<RoutingResponse> createListRouting(
            @RequestParam(required = false) MultipartFile file) throws Exception {
        if (file != null) {
            return new ApiResponse<RoutingResponse>()
                    .success(routingService.routing(file.getInputStream()));
        } else {
            routingService.routing();
        }
        return new ApiResponse<RoutingResponse>().success(null);
    }

    @GetMapping("/vehicles")
    public ApiResponse<List<Vehicle>> getVehicleList(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "100") int limit) {
        List<Vehicle> vehicles = vehicleService.getVehicleList(offset, limit);
        return new ApiResponse<List<Vehicle>>().success(vehicles);
    }

}
