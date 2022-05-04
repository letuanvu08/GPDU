import models.Coordinates;
import models.Order;
import models.Vehicle;

import java.util.concurrent.ThreadLocalRandom;

public class Generator {
    public static final double MIN_LAT = 10.705198;
    public static final double MIN_LNG = 106.689546;
    public static final double MAX_LAT = 10.751411;
    public static final double MAX_LNG = 106.743105;
    public static final int MIN_TIME_WINDOW = 60 * 60;
    public static final int MAX_TIME_WINDOW = 3 * 60 * 60;
    public static final int MIN_WEIGHT = 4;
    public static final int MAX_WEIGHT = 12;
    public static final int TIME_RANGE = 22 * 60 * 60;
    public static final int MIN_DELIVERY_TIME = 5 * 60 * 60;

    public static Order genRandomOrder(int index) {
        int pickupEarliestTime = ThreadLocalRandom.current()
                .nextInt(0, TIME_RANGE - 2 * MAX_TIME_WINDOW - MIN_DELIVERY_TIME);
        int pickupLatestTime = ThreadLocalRandom.current()
                .nextInt(pickupEarliestTime + MIN_TIME_WINDOW, pickupEarliestTime + MAX_TIME_WINDOW);
        int deliveryEarliestTime = ThreadLocalRandom.current()
                .nextInt(pickupLatestTime + MIN_DELIVERY_TIME, TIME_RANGE - MAX_TIME_WINDOW);
        int deliveryLatestTime = ThreadLocalRandom.current()
                .nextInt(deliveryEarliestTime + MIN_TIME_WINDOW, deliveryEarliestTime + MAX_TIME_WINDOW);
        return Order.builder()
                .id(index)
                .weight(ThreadLocalRandom.current().nextInt(MIN_WEIGHT, MAX_WEIGHT))
                .pickup(Order.Node.builder()
                        .earliestTime(pickupEarliestTime)
                        .latestTime(pickupLatestTime)
                        .coordinates(genRandomCoordinates())
                        .build())
                .delivery(Order.Node
                        .builder()
                        .earliestTime(deliveryEarliestTime)
                        .latestTime(deliveryLatestTime)
                        .coordinates(genRandomCoordinates())
                        .build())
                .build();
    }

    public static Vehicle genRandomVehicle(int index) {
        return Vehicle.builder()
                .id(index)
                .coordinates(genRandomCoordinates())
                .build();
    }

    public static Coordinates genRandomCoordinates() {
        return Coordinates.builder()
                .latitude(ThreadLocalRandom.current().nextDouble(MIN_LAT, MAX_LAT))
                .longitude(ThreadLocalRandom.current().nextDouble(MIN_LNG, MAX_LNG)).build();
    }
}
