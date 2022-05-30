package hcmut.thesis.gpduserver.config.cache;

import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Routing;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutingCache {
    private final List<Order> ordersCache = new ArrayList<>();

    private final List<Routing> routes = new ArrayList<>();

    private final List<Vehicle> vehicles;

    public RoutingCache(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void push(Order order){
        ordersCache.add(order);
    }

    public void cleanCache(){
        ordersCache.clear();
    }

}
