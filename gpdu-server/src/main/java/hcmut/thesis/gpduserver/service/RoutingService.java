package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.models.entity.Routing;
import java.util.List;

public interface RoutingService {

  Routing getRoutingActiveByVehicleId(String vehicleId);
  List<Routing> getListRoutingByVehicle(String vehicleId, int offset, int limit);
}
