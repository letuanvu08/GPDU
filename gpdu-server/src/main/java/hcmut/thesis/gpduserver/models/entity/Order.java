package hcmut.thesis.gpduserver.models.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.thesis.gpduserver.mongodb.generic.PO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Order extends PO {
  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;
  private String userName;
  private String userId;
  private Node pickup;
  private Node delivery;
  private String note;
  private String vehicleId;
  private Location currentLocation;
  private Package packageInfo;
  private Status currentStep;
  private String currentStatus;
  private List<Status> historyStatus;


  @Embedded
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Data
  public static class Package{
    private String category;
    private String name;
    private Long weight;
  }

  @Embedded
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Data
  public static class Status{
    private String step;
    private String status;
    private Long timestamp;
  }



}