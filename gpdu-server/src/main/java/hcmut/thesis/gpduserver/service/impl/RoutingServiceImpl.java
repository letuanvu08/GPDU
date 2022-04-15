package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.constants.mongodb.QueryOperators;
import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.models.request.routing.RequestCreateRouting;
import hcmut.thesis.gpduserver.repository.RoutingRepository;
import hcmut.thesis.gpduserver.service.OrderService;
import hcmut.thesis.gpduserver.service.RoutingService;
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
public class RoutingServiceImpl implements RoutingService {

  @Autowired
  private RoutingRepository routingRepository;
  @Autowired
  private OrderService orderService;

  @Override
  public List<Routing> createListRouting(List<RequestCreateRouting> requests) {
    List<Routing> routings = new ArrayList<>();
    try {
      closeAllActiveRouting();
      requests.forEach(request -> {
        routings.add(createRouting(request));
      });
    } catch (Exception e) {
      log.info("Error when createRouting, request: {}, exception: {}",
          GsonUtils.toJsonString(requests), e.getMessage());
    }
    return routings;
  }

  private Routing createRouting(RequestCreateRouting request) {
    try {
      Routing routingRequest = Routing.builder()
          .active(true)
          .nodes(request.getNodes())
          .listOrderId(request.getListOrderId())
          .vehicleId(request.getVehicleId())
          .build();
      Optional<Routing> routing = routingRepository.insert(routingRequest);
      routing.ifPresent(value -> request.getListOrderId().forEach(orderId ->
          orderService.updateOrderRouting(orderId, value.getId().toString())));
      log.info("createRouting, request: {}. result: {}", GsonUtils.toJsonString(request),
          GsonUtils.toJsonString(routing.orElse(null)));
      return routing.orElse(null);
    } catch (Exception e) {
      log.info("Error when createRouting, request: {}, exception: {}",
          GsonUtils.toJsonString(request), e.getMessage());
      return null;
    }
  }

  private void closeAllActiveRouting() {
    try {
      Document request = new Document().append("active", true);
      Document update = new Document().append("active", false);
      routingRepository.updateMany(request, update);
    } catch (Exception e) {
      log.info("Error when closeAllActiveRouting, exception: {}", e.getMessage());
    }
  }

  @Override
  public Routing getRoutingActiveByVehicleId(String vehicleId) {
    Document request = new Document().append("vehicleId", vehicleId).append("active", true);
    return getRoutingByRequest(request);
  }

  @Override
  public Routing getRoutingActiveByOrderId(String orderId) {
    Document request = new Document()
        .append("listOrderId", new Document().append(QueryOperators.ALL, List.of(orderId)))
        .append("active", true);
    return getRoutingByRequest(request);
  }

  @Override
  public Routing getRoutingByRequest(Document request) {
    try {
      Optional<Routing> routingOptional = routingRepository.getByQuery(request);
      log.info("getRoutingByRequest, request: {}, result: {}",
          GsonUtils.toJsonString(request), GsonUtils.toJsonString(routingOptional.orElse(null)));
      return routingOptional.orElse(null);
    } catch (Exception e) {
      log.info("Error when getOrderByRequest, request: {}, exception: {}",
          GsonUtils.toJsonString(request),
          e.getMessage());
      return null;
    }
  }

  @Override
  public List<Routing> getListRoutingByVehicle(String vehicleId, int offset, int limit) {
    try {
      Document request = new Document().append("vehicleId", vehicleId);
      Optional<List<Routing>> routingList = routingRepository.getMany(request, new Document(),
          offset,
          limit);
      log.info("getListRoutingByVehicleId: {}, offset: {}, limit: {},  result: {}",
          vehicleId, offset, limit, GsonUtils.toJsonString(routingList.orElse(new ArrayList<>())));
      return routingList.orElse(new ArrayList<>());
    } catch (Exception e) {
      log.error("Error when getListRoutingByVehicleId: {}, offset: {}, limit: {}, exception: {}",
          vehicleId, offset, limit, e.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public Boolean updateActiveRouting(String routingId, Boolean active) {
    try {
      Optional<Routing> routingOptional = routingRepository.getById(routingId);
      if (routingOptional.isEmpty()) {
        log.info("UpdateActiveRouting routingId: {}, not found", routingId);
        return false;
      }
      Routing routing = routingOptional.get();
      routing.setActive(active);
      return updateRouting(routing);
    } catch (Exception e) {
      log.info("updateActiveRouting id: {}, exception: {}", routingId, e.getMessage());
      return false;
    }
  }

  @Override
  public Boolean updateRouting(Routing routing) {
    try {
      Optional<Boolean> result = routingRepository.update(routing.getId().toString(), routing);
      log.info("updateRouting, routing: {}, result: {}", routing,
          GsonUtils.toJsonString(result.orElse(false)));
      return result.orElse(false);
    } catch (Exception e) {
      log.info("Error updateRouting, routing: {}, exception: {}", routing, e.getMessage());
      return false;
    }
  }
}
