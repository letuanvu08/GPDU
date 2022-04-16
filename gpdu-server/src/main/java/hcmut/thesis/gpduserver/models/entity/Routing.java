package hcmut.thesis.gpduserver.models.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.thesis.gpduserver.constants.enumations.TypeNode;
import hcmut.thesis.gpduserver.mongodb.generic.PO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Routing extends PO {

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;
  private String vehicleId;
  @Builder.Default
  private Boolean active = true;
  private NodeRouting nextNode;
  private List<NodeRouting> nodes;

  @Embedded
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @SuperBuilder
  public static class NodeRouting extends Node{
    private String orderId;
    private TypeNode typeNode;
  }
}
