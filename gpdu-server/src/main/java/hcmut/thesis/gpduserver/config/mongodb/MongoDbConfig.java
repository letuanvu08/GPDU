package hcmut.thesis.gpduserver.config.mongodb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mongodb")
public class MongoDbConfig {
  private String database;
}
