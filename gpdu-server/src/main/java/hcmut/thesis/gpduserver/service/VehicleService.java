package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.models.request.vehicle.FormAddVehicle;
import java.util.List;

public interface VehicleService {
  Vehicle addVehicle(FormAddVehicle form);

  List<Vehicle> getVehicleList(int offset, int limit);

  Vehicle getVehicleByOwnerId(String ownerId);

  Vehicle getVehicleById(String vehicleId);

  Boolean updateCurrentLocationVehicle(String vehicleId, Location location);

  Boolean updateVehicle(Vehicle vehicle);

}
