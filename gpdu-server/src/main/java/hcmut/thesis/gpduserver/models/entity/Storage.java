package hcmut.thesis.gpduserver.models.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.thesis.gpduserver.mongodb.generic.PO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;


@Embedded
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Storage extends PO {

  private String address;
  private String phone;
  private String owner;
  private Location location;

}
