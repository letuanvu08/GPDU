package hcmut.thesis.gpduserver.controller.rest;

import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import hcmut.thesis.gpduserver.models.request.routing.RequestCreateRouting;
import hcmut.thesis.gpduserver.models.request.vehicle.FormAddVehicle;
import hcmut.thesis.gpduserver.service.OrderService;
import hcmut.thesis.gpduserver.service.RoutingService;
import hcmut.thesis.gpduserver.service.UserService;
import hcmut.thesis.gpduserver.service.VehicleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  @Autowired
  private VehicleService vehicleService;

  @Autowired
  private UserService userService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private RoutingService routingService;

  @PostMapping("/vehicles")
  public ApiResponse<Vehicle> addVehicle(@RequestBody FormAddVehicle formAddVehicle) {
    Vehicle vehicle = vehicleService.addVehicle(formAddVehicle);
    return new ApiResponse<Vehicle>().success(vehicle);
  }

  //   cái này dùng để test trong khi chưa có AI
  @PostMapping("/routing")
  public ApiResponse<List<Routing>> createListRouting(
      @RequestBody List<RequestCreateRouting> requests) {
    routingService.routing();
    return new ApiResponse<List<Routing>>().success(null);
  }

}
