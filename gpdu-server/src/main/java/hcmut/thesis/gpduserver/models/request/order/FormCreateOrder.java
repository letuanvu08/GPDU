package hcmut.thesis.gpduserver.models.request.order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hcmut.thesis.gpduserver.models.entity.Node;
import hcmut.thesis.gpduserver.models.entity.Order.Package;
import lombok.Data;

@Data
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
