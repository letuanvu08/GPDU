package hcmut.thesis.gpduserver.config.grpc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "grpc.ai-service")
@Data
public class AIServiceGrpcProperties {
  private String target;
  private boolean useSSL;
  private int poolSize;
  private int deadline = 10000;
}
