package hcmut.thesis.gpduserver.utils.duration;

import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.entity.Order;
import hcmut.thesis.gpduserver.models.entity.Vehicle;
import java.util.ArrayList;
import java.util.List;


public class DurationCalculator {

    public static final int AVERAGE_VELOCITY = 9; //m/s

    private static int calDistance(Location c1, Location c2) {
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

    private static int calDuration(Location c1, Location c2) {
        return calDistance(c1, c2) / AVERAGE_VELOCITY;
    }

    public static List<List<Float>> getOrderDurationMatrix(List<Order> orders) {
        List<List<Float>> result = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            List<Float> pickup = new ArrayList<>();
            for (Order order : orders) {
                pickup.add((float) calDuration(orders.get(i).getPickup().getLocation(),
                    order.getPickup().getLocation()));
                pickup.add((float) calDuration(orders.get(i).getPickup().getLocation(),
                    order.getDelivery().getLocation()));
            }
            List<Float> delivery = new ArrayList<>();
            for (Order order : orders) {
                delivery.add((float) calDuration(orders.get(i).getDelivery().getLocation(),
                    order.getPickup().getLocation()));
                delivery.add((float) calDuration(orders.get(i).getDelivery().getLocation(),
                    order.getDelivery().getLocation()));
            }
            result.add(pickup);
            result.add(delivery);
        }
        return result;
    }

    public static List<List<Float>> getVehicleDurationMatrix(List<Vehicle> vehicles,
        List<Order> orders) {
        List<List<Float>> result = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            List<Float> row = new ArrayList<>();
            for (int j = 0; j < orders.size(); j++) {
                row.add((float) calDuration(vehicles.get(i).getCurrentLocation(),
                    orders.get(j).getPickup().getLocation()));
                row.add((float) calDuration(vehicles.get(i).getCurrentLocation(),
                    orders.get(j).getDelivery().getLocation()));
            }
            result.add(row);
        }
        return result;
    }

    public static List<Float> getRepoDurationList(Location repoCoordinates, List<Order> orders) {
        List<Float> result = new ArrayList<>();
        for (int j = 0; j < orders.size(); j++) {
            result.add(
                (float) calDuration(repoCoordinates, orders.get(j).getPickup().getLocation()));
            result.add(
                (float) calDuration(repoCoordinates, orders.get(j).getDelivery().getLocation()));
        }
        return result;
    }

    public static List<Float> getVehicleRepoDurationList(Location repoCoordinates,
        List<Vehicle> vehicles) {
        List<Float> result = new ArrayList<>();
        for (int j = 0; j < vehicles.size(); j++) {
            result.add((float) calDuration(repoCoordinates, vehicles.get(j).getCurrentLocation()));
        }
        return result;
    }
}
