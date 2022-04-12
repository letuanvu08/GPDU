package hcmut.thesis.gpduserver.models.entity;

import eu.dozd.mongo.annotation.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embedded
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {

  private String customerName;
  private String phone;
  private String address;
  private Location location;
  private Long earliestTime;
  private Long latestTime;
  private String vehicleId;
}

