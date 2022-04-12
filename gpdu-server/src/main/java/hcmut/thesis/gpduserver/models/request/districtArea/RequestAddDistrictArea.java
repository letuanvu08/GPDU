package hcmut.thesis.gpduserver.models.request.districtArea;

import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Storage;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestAddDistrictArea {

  private List<Storage> storages;
  private List<Location> boundary;
}
