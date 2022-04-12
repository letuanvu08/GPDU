package hcmut.thesis.gpduserver.models.reponse.mapBox;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapBoxResponse {

  private List<Routes> routes;
  private String code;
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Routes{
    private Double weightTypical;
    private Double durationTypical;
    private Double weight;
    private Double duration;
    private Double distance;
  }
}
