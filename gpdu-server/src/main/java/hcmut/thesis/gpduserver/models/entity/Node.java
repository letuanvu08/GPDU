package hcmut.thesis.gpduserver.models.entity;

import eu.dozd.mongo.annotation.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Embedded
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node implements Comparable<Node>{

  private String customerName;
  private String phone;
  private String address;
  private Location location;
  private Long earliestTime;
  private Long latestTime;


  @Override
  public int compareTo(Node other) {
    return (int) (earliestTime-other.getEarliestTime());
  }
}

