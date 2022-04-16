package hcmut.thesis.gpduserver.ai.models;

import hcmut.thesis.gpduserver.constants.enumations.TypeNode;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class Key<T> {
    private T value;
    private TypeNode type; // pickup, delivery
    private Integer orderIndex;
}
