package hcmut.thesis.gpduserver.models.request.vehicle;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Id;
import hcmut.thesis.gpduserver.models.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormAddVehicle {

  private String type;
  private Long capacity;
  private Long volume;
  private Location currentLocation;
  private String ownerId;
}
