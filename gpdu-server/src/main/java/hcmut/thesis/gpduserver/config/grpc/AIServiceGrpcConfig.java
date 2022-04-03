package hcmut.thesis.gpduserver.config.grpc;


import hcmut.ai_service.AIServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIServiceGrpcConfig {
  @Autowired
  private AIServiceGrpcProperties properties;
  @Bean
  public AIServiceGrpc.AIServiceBlockingStub AIServiceBlockingStub(){
    Channel channel = ManagedChannelBuilder.forTarget(properties.getTarget())
            .usePlaintext()
            .build();
    return AIServiceGrpc.newBlockingStub(channel);
  }

}
