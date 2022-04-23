package hcmut.thesis.gpduserver.models.request.order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hcmut.thesis.gpduserver.models.entity.Node;
import hcmut.thesis.gpduserver.models.entity.Order.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormCreateOrder {
  @JsonIgnore
  private String userName;
  @JsonIgnore
  private String userId;
  private Node pickup;
  private Node delivery;
  private String note;
  private Package packageInfo;

}
