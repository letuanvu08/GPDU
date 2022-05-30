package hcmut.thesis.gpduserver.models.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.thesis.gpduserver.mongodb.generic.PO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class Vehicle  extends PO {
  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;
  private String type;
  private Long capacity;
  private Long volume;
  private Location currentLocation;
  private String ownerId;
  private Long startTime;
}


