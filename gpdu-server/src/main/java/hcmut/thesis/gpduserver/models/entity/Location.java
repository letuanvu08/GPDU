package hcmut.thesis.gpduserver.models.entity;

import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embedded
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Float latitude;
    private Float longitude;
}
