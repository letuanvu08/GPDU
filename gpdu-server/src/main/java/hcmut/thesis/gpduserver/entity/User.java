package hcmut.thesis.gpduserver.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.thesis.gpduserver.mongodb.generic.PO;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Entity
@Data
@Builder
public class User extends PO {
  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;
  private String userName;
  private String password;
  private String email;
  private List<String> roles;
}
