package hcmut.thesis.gpduserver.config.cache;

import java.util.List;
import lombok.Getter;

@Getter
public class RoutingCache {
    private final List<Long> ordersCache;

    public RoutingCache(List<Long> ordersCache) {
        this.ordersCache = ordersCache;
    }

    public void push(Long order){
        ordersCache.add(order);
    }

    public void cleanCache(){
        ordersCache.clear();
    }

}
