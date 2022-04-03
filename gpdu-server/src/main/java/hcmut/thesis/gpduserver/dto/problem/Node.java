package hcmut.thesis.gpduserver.dto.problem;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class Node {
  private double lat;
  private double lgn;
  private Timestamp earliestTime;
  private Timestamp latestTime;
}
