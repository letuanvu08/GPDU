package hcmut.thesis.gpduserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.mapbox")
@Data
public class MapboxConfig {

  private String token;
  private String domain;
  private String scheme;
  private String pathOptimized;
  private String pathDirections;
}
