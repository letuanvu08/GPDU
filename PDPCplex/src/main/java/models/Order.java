package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private Node pickup;
    private Node delivery;
    private Integer vehicleId;
    @Builder.Default
    private Boolean vehicleConstant = false;
    private Long weight;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        private Location location;
        private Long earliestTime;
        private Long latestTime;
    }
}
