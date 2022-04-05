package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.models.request.vehicle.FormAddVehicle;
import hcmut.thesis.gpduserver.repository.VehicleRepository;
import hcmut.thesis.gpduserver.service.UserService;
import hcmut.thesis.gpduserver.service.VehicleService;
import hcmut.thesis.gpduserver.utils.GsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VehicleServiceImpl implements VehicleService {

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private UserService userService;

  @Override
  public Vehicle addVehicle(FormAddVehicle form) {
    try {
      Vehicle vehicleRequest = Vehicle.builder()
          .ownerId(form.getOwnerId())
          .type(form.getType())
          .capacity(form.getCapacity())
          .volume(form.getVolume())
          .currentLocation(form.getCurrentLocation())
          .build();
      Optional<Vehicle> vehicle = vehicleRepository.insert(vehicleRequest);
      log.info("addVehicle form: {}, result: {}", form,
          GsonUtils.toJsonString(vehicle.orElse(null)));
      vehicle.ifPresent(
          value -> userService.assignVehicleForUser(value.getId().toString(), value.getOwnerId()));
      return vehicle.orElse(null);
    } catch (Exception e) {
      log.error("Error when addVehicle form: {}, exception: {}", form, e.getMessage());
      return null;
    }
  }

  @Override
  public List<Vehicle> getVehicleList(int offset, int limit) {
    try {
      Optional<List<Vehicle>> vehicles = vehicleRepository.getMany(new Document(), new Document(),
          offset, limit);
      log.info("getVehicleList offset: {}, limit: {}, result: {}", offset, limit,
          GsonUtils.toJsonString(vehicles));
      return vehicles.orElse(new ArrayList<>());
    } catch (Exception e) {
      log.error("Error when getVehicleList offset: {}, limit: {}, exception: {}", offset, limit,
          e.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public Vehicle getVehicleByOwnerId(String ownerId) {
    try {
      Document request = new Document().append("ownerId", ownerId);
      Optional<Vehicle> vehicle = vehicleRepository.getByQuery(request);
      log.info("getVehicleByOwnerId ownerId: {}, result: {}", ownerId,
          GsonUtils.toJsonString(vehicle.orElse(null)));
      return vehicle.orElse(null);
    } catch (Exception e) {
      log.error("Error when getVehicleByOwnerId ownerId: {}, exception: {}", ownerId,
          e.getMessage());
      return null;
    }
  }

  @Override
  public Vehicle getVehicleById(String vehicleId) {
    try {
      Optional<Vehicle> vehicle = vehicleRepository.getById(vehicleId);
      log.info("getVehicleByOwnerId vehicleId: {}, result: {}", vehicleId,
          GsonUtils.toJsonString(vehicle.orElse(null)));
      return vehicle.orElse(null);
    } catch (Exception e) {
      log.error("Error when getVehicleByOwnerId vehicleId: {}, exception: {}", vehicleId,
          e.getMessage());
      return null;
    }
  }

  @Override
  public Boolean updateCurrentLocationVehicle(String vehicleId, Location location) {
    try {
      Optional<Vehicle> vehicleOptional = vehicleRepository.getById(vehicleId);
      if (vehicleOptional.isEmpty()) {
        log.info("updateCurrentLocationVehicle vehicleId: {} not found", vehicleId);
        return null;
      }
      Optional<Boolean> result = vehicleRepository.update(vehicleId, vehicleOptional.get());
      log.info("updateCurrentLocationVehicle vehicleId: {}, location :{}, vehicle: {}, result: {}",
          vehicleId,
          GsonUtils.toJsonString(location), GsonUtils.toJsonString(vehicleOptional.get()),
          result.orElse(false));
      return result.orElse(false);
    } catch (Exception e) {
      log.error("pdateCurrentLocationVehicle vehicleId: {}, exception: {}", vehicleId,
          e.getMessage());
      return false;
    }
  }
}
