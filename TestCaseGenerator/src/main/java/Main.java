import models.Coordinates;
import models.Order;
import models.Vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static final int ORDER_NUMBER = 7;
    public static final int VEHICLE_CAPACITY = 50;
    public static final int VEHICLE_NUMBER = 2;

    public static void main(String[] args) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("testcase.txt"))) {
            writer.write("OrderNumber:\t" + ORDER_NUMBER);
            writer.newLine();
            writer.write(String.format("%-10s%-20s%-20s%-20s%-20s%-20s%-22s%-22s%-22s%-22s",
                    "Id", "OrderWeight", "PickupLatitude", "PickupLongitude", "PickupEarliestTime", "PickupLatestTime",
                    "DeliveryLatitude", "DeliveryLongitude", "DeliveryEarliestTime", "DeliveryLatestTime"));
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < ORDER_NUMBER; i++) {
                writer.newLine();
                Order order = Generator.genRandomOrder(i);
                writer.write(String.format("%-10s%-20s%-20s%-20s%-20s%-20s%-22s%-22s%-22s%-22s",
                        order.getId(), order.getWeight(), order.getPickup().getCoordinates().getLatitude(),
                        order.getPickup().getCoordinates().getLongitude(), order.getPickup().getEarliestTime(),
                        order.getPickup().getLatestTime(), order.getDelivery().getCoordinates().getLatitude(),
                        order.getDelivery().getCoordinates().getLongitude(), order.getDelivery().getEarliestTime(),
                        order.getDelivery().getLatestTime()
                ));
                orders.add(order);
            }
            writer.newLine();
            writer.write("OrderDurationMatrix");
            writer.newLine();
            writer.write(StringUtils.convertDurationMatrix2String(DurationCalculator.getOrderDurationMatrix(orders)));
            writer.write("VehicleCapacity:\t" + VEHICLE_CAPACITY);
            writer.newLine();
            writer.write("VehicleNumber:\t" + VEHICLE_NUMBER);
            writer.newLine();
            writer.write(String.format("%-10s%-20s%-20s", "Id", "Latitude", "Longitude"));
            List<Vehicle> vehicles = new ArrayList<>();
            for (int i = 0; i < VEHICLE_NUMBER; i++) {
                Vehicle vehicle = Generator.genRandomVehicle(i);
                writer.newLine();
                writer.write(String.format("%-10s%-20s%-20s", vehicle.getId(), vehicle.getCoordinates().getLatitude(),
                        vehicle.getCoordinates().getLongitude()));
                vehicles.add(vehicle);
            }
            writer.newLine();
            writer.write("VehicleDurationMatrix");
            writer.newLine();
            writer.write(StringUtils.convertDurationMatrix2String(DurationCalculator.
                    getVehicleDurationMatrix(vehicles, orders)));
            Coordinates repoCoordinates = Generator.genRandomCoordinates();
            writer.write(String.format("RepoCoordinates:\t%s,%s",
                    repoCoordinates.getLatitude(), repoCoordinates.getLongitude()));
            writer.newLine();
            writer.write("RepoDurationList");
            writer.newLine();
            writer.write(StringUtils.convertDurationMatrix2String(List.of(DurationCalculator.
                    getRepoDurationList(repoCoordinates, orders))));
            writer.write("VehicleRepoDurationList");
            writer.newLine();
            writer.write(StringUtils.convertDurationMatrix2String(List.of(DurationCalculator
                    .getVehicleRepoDurationList(repoCoordinates, vehicles))));
            writer.write("TravelCost:\t" + 2);
            writer.newLine();
            writer.write("WaitingCost:\t" + 0.01);
            writer.newLine();
            writer.write("LateCost:\t" + 0.02);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
