package hcmut.thesis.gpduserver.ai.models;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class Key {
    private Integer value;
    private String type; // pickup, delivery
    private Integer orderIndex;
}
