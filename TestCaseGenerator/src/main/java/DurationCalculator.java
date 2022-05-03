import models.Coordinates;
import models.Order;
import models.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class DurationCalculator {
    public static final int AVERAGE_VELOCITY = 9; //m/s

    private static int calDistance(Coordinates c1, Coordinates c2) {
        double R = 6371000;
        double p1 = c1.getLatitude() * Math.PI / 180;
        double p2 = c2.getLatitude() * Math.PI / 180;
        double deltaP = (c2.getLatitude() - c1.getLatitude()) * Math.PI / 180;
        double deltaL = (c2.getLongitude() - c1.getLongitude()) * Math.PI / 180;
        double a = Math.sin(deltaP / 2) * Math.sin(deltaP / 2) +
                Math.cos(p1) * Math.cos(p2) * Math.sin(deltaL / 2) * Math.sin(deltaL / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (R * c);
    }

    private static int calDuration(Coordinates c1, Coordinates c2) {
        return calDistance(c1, c2) / AVERAGE_VELOCITY;
    }

    public static List<List<Integer>> getOrderDurationMatrix(List<Order> orders) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            List<Integer> pickup = new ArrayList<>();
            for (Order order : orders) {
                pickup.add(calDuration(orders.get(i).getPickup().getCoordinates(), order.getPickup().getCoordinates()));
                pickup.add(calDuration(orders.get(i).getPickup().getCoordinates(), order.getDelivery().getCoordinates()));
            }
            List<Integer> delivery = new ArrayList<>();
            for (Order order : orders) {
                delivery.add(calDuration(orders.get(i).getDelivery().getCoordinates(),
                        order.getPickup().getCoordinates()));
                delivery.add(calDuration(orders.get(i).getDelivery().getCoordinates(),
                        order.getDelivery().getCoordinates()));
            }
            result.add(pickup);
            result.add(delivery);
        }
        return result;
    }

    public static List<List<Integer>> getVehicleDurationMatrix(List<Vehicle> vehicles, List<Order> orders) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < orders.size(); j++) {
                row.add(calDuration(vehicles.get(i).getCoordinates(), orders.get(j).getPickup().getCoordinates()));
                row.add(calDuration(vehicles.get(i).getCoordinates(), orders.get(j).getDelivery().getCoordinates()));
            }
            result.add(row);
        }
        return result;
    }
}
