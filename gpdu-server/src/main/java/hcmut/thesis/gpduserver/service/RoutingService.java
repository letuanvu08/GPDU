package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.models.request.routing.RequestCreateRouting;

import java.util.List;

import org.bson.Document;

public interface RoutingService {

    List<Routing> createListRouting(List<Routing> request);

    Routing getRoutingActiveByVehicleId(String vehicleId);

    Routing getRoutingActiveByOrderId(String orderId);

    Routing getRoutingByRequest(Document request);

    List<Routing> getListRoutingByVehicle(String vehicleId, int offset, int limit);

    Boolean updateActiveRouting(String routingId, Boolean active);

    Boolean updateRouting(Routing routing);

    void routing();


}
