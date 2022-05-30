package hcmut.thesis.gpduserver.config.cache;

import hcmut.thesis.gpduserver.models.entity.Vehicle;
import hcmut.thesis.gpduserver.service.VehicleService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dynamic-routing")
@Getter
@Setter
public class DynamicRoutingConfig {

    private float threshold;
    private float weightTime;

    @Autowired
    private VehicleService vehicleService;


    @Bean
    public RoutingCache getRoutingCache() {
            List<Vehicle> vehicles = vehicleService.getVehicleList(0,5);

        return new RoutingCache(vehicles);
    }

}
