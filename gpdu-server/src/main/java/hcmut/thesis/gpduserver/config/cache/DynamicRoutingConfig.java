package hcmut.thesis.gpduserver.config.cache;

import java.util.ArrayList;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dynamic-routing")
@Getter
public class DynamicRoutingConfig {

    private float threshold;
    private float weightTime;

    @Bean
    public RoutingCache getRoutingCache() {
        return new RoutingCache(new ArrayList<>());
    }

}
