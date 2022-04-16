package hcmut.thesis.gpduserver.ai.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntegerRouting implements Comparable<IntegerRouting> {

    private int vehicle;
    private int randomKey;

    @Override
    public int compareTo(IntegerRouting other) {
        if(this.vehicle != other.vehicle){
            return this.vehicle - other.vehicle;
        }
        if (this.randomKey!= other.randomKey){
            return this.randomKey - other.randomKey;
        }
        return 0;
    }
}
