package hcmut.thesis.gpduserver.models.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.thesis.gpduserver.mongodb.generic.PO;
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
public class User extends PO {
  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;
  private String userName;
  private String password;
  private String email;
  private String typeUser;

}
