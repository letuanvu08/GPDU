package models;

import enums.NodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    private Location location;
    private Node nextNode;
    private Long load;
    private Long volume;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Node {
        private NodeType type;
        private Integer orderId;
    }
}
