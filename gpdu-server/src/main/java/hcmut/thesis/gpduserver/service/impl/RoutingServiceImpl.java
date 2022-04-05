package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.service.RoutingService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoutingServiceImpl implements RoutingService {

  @Override
  public Routing getRoutingActiveByVehicleId(String vehicleId) {
    return null;
  }

  @Override
  public List<Routing> getListRoutingByVehicle(String vehicleId, int offset, int limit) {
    return null;
  }
}
