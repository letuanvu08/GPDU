package hcmut.thesis.gpduserver.service.impl;

import hcmut.ai_service.AIServiceGrpc;
import hcmut.ai_service.AiService;
import hcmut.thesis.gpduserver.dto.problem.Node;
import hcmut.thesis.gpduserver.dto.problem.PickupDelivery;
import hcmut.thesis.gpduserver.dto.problem.Vehicle;
import hcmut.thesis.gpduserver.dto.problem.reponse.ProblemResponseDto;
import hcmut.thesis.gpduserver.dto.problem.request.ProblemRequestDto;
import hcmut.thesis.gpduserver.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AIServiceImpl implements AIService {
  @Autowired
  private AIServiceGrpc.AIServiceBlockingStub aiServiceBlockingStub;


  @Override
  public ProblemResponseDto submitProblem(ProblemRequestDto problemRequestDto) {
    ProblemResponseDto responseDto = ProblemResponseDto.builder().build();
    try {
      log.info("Submit Problem with request: {}", problemRequestDto);
      AiService.ProblemForm request = AiService.ProblemForm.newBuilder()
              .setNumberNode(problemRequestDto.getNumberNode())
              .setNumberVehicle(problemRequestDto.getNumberNode())
              .addAllVehicles(convertListVehicle(problemRequestDto.getVehicles()))
              .addAllPickupDelivery(convertListPickupDelivery(problemRequestDto.getPickupDeliveries()))
              .build();
      AiService.ProblemResponse response = aiServiceBlockingStub.submitProblem(request);
      responseDto = convertResponse(response);
      log.info("Submit problem response: {}", responseDto);
    } catch (Exception e) {
      log.error("Submit problem error: {}", e);
    }
    return responseDto;
  }

  private ProblemResponseDto convertResponse(AiService.ProblemResponse response) {
    return ProblemResponseDto.builder()
            .status(response.getStatus())
            .message(response.getMessage())
            .code(response.getCode())
            .build();
  }

  private List<AiService.Vehicle> convertListVehicle(List<Vehicle> vehicles) {
    return vehicles.stream().map(AIServiceImpl::convertVehicle).collect(Collectors.toList());
  }

  private static AiService.Vehicle convertVehicle(Vehicle vehicle) {
    return AiService.Vehicle.newBuilder()
            .setId(vehicle.getId())
            .setLat(vehicle.getLat())
            .setLgn(vehicle.getLgn())
            .build();
  }

  private List<AiService.Pickup_Delivery> convertListPickupDelivery(List<PickupDelivery> pickupDeliveries) {
    return pickupDeliveries.stream().map(this::convertPickupDelivery).collect(Collectors.toList());
  }

  private AiService.Pickup_Delivery convertPickupDelivery(PickupDelivery pickupDeliveries) {
    return AiService.Pickup_Delivery.newBuilder()
            .setDelivery(convertNode(pickupDeliveries.getDelivery()))
            .setPickup(convertNode(pickupDeliveries.getPickup()))
            .setVolume(pickupDeliveries.getVolume())
            .setWeight(pickupDeliveries.getWeight())
            .build();
  }

  private static AiService.Node convertNode(Node node) {
    return AiService.Node.newBuilder()
            .setLat(node.getLat())
            .setLgn(node.getLgn())
            .setEarliestTime(node.getEarliestTime().toString())
            .setLatestTime(node.getLatestTime().toString())
            .build();
  }
}
