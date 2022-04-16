package hcmut.thesis.gpduserver.controller.rest;


import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import hcmut.thesis.gpduserver.service.VehicleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

  @Autowired
  private VehicleService vehicleService;

  @GetMapping("/{vehicleId}")
  public ApiResponse<Vehicle> getVehicleById(@PathVariable String vehicleId) {
    Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
    return new ApiResponse<Vehicle>().success(vehicle);
  }

  @GetMapping("/users/{ownerId}")
  public ApiResponse<Vehicle> getVehicleByOwnerId(@PathVariable String ownerId) {
    Vehicle vehicle = vehicleService.getVehicleByOwnerId(ownerId);
    return new ApiResponse<Vehicle>().success(vehicle);
  }

  @GetMapping("")
  public ApiResponse<List<Vehicle>> getVehicleList(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "100") int limit) {
    List<Vehicle> vehicles = vehicleService.getVehicleList(offset, limit);
    return new ApiResponse<List<Vehicle>>().success(vehicles);
  }

  @PostMapping("/{vehicleId}/location")
  public ApiResponse<Boolean> updateLocationVehicle(
      @PathVariable String vehicleId,
      @RequestBody Location location) {
    Boolean result = vehicleService.updateCurrentLocationVehicle(vehicleId, location);
    return new ApiResponse<Boolean>().success(result);
  }

}
