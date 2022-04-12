package hcmut.thesis.gpduserver.models.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.thesis.gpduserver.mongodb.generic.PO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictArea  extends PO {
  @Id
  private String districtName;
  private List<Location> boundary;
  private List<Storage> storages;
}
