package hcmut.thesis.gpduserver.controller.rest;

import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.models.entity.UserSecure;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import hcmut.thesis.gpduserver.service.RoutingService;
import hcmut.thesis.gpduserver.service.VehicleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rouging")
public class RoutingController {

  @Autowired
  private RoutingService routingService;
  @Autowired
  private VehicleService vehicleService;

  @GetMapping("/vehicle/{vehicleId}")
  public ApiResponse<Routing> getRoutingActiveByVehicelid(@PathVariable String vehicleId) {
    Routing routing = routingService.getRoutingActiveByVehicleId(vehicleId);
    return new ApiResponse<Routing>().success(routing);
  }

  @GetMapping("/")
  public ApiResponse<List<Routing>> getRoutingList(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "100") int limit,
      Authentication authentication) {
    UserSecure userSecure = (UserSecure) authentication.getPrincipal();
    Vehicle vehicle = vehicleService.getVehicleByOwnerId(userSecure.getId());
    List<Routing> routingList = routingService.getListRoutingByVehicle(vehicle.getId().toString(),
        offset, limit);
    return new ApiResponse<List<Routing>>().success(routingList);
  }

}
