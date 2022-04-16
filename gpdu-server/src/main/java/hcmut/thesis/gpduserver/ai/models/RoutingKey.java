package hcmut.thesis.gpduserver.ai.models;

import hcmut.thesis.gpduserver.constants.enumations.TypeNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoutingKey {
    private TypeNode type;
    private Integer orderId;
}
