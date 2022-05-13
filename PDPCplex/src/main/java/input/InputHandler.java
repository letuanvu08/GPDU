package input;

import models.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputHandler {
    public static Input handle(String fileName) {
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            int orderNumber = Integer.parseInt(br.readLine().split("\t")[1]);
            br.readLine();
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < orderNumber; i++) {
                Order routingOrder = InputConverter.convertString2Order(br.readLine());
                orders.add(routingOrder);
            }
            br.readLine();
            List<List<Float>> orderNodeMatrix = new ArrayList<>();
            for (int i = 0; i < orderNumber * 2; i++) {
                orderNodeMatrix.add(Arrays.stream(br.readLine().split(","))
                        .map(Float::parseFloat).collect(Collectors.toList()));
            }
            long capacity = Long.parseLong(br.readLine().split("\t")[1]);
            int vehicleNumber = Integer.parseInt(br.readLine().split("\t")[1]);
            br.readLine();
            List<Vehicle> vehicles = new ArrayList<>();
            for (int i = 0; i < vehicleNumber; i++) {
                Vehicle routingVehicle = Vehicle.builder()
                        .load(capacity)
                        .location(InputConverter.convertString2VehicleLocation(br.readLine()))
                        .build();
                vehicles.add(routingVehicle);
            }
            br.readLine();
            List<List<Float>> vehicleMatrix = new ArrayList<>();
            for (int i = 0; i < vehicleNumber; i++) {
                vehicleMatrix.add(Arrays.stream(br.readLine().split(","))
                        .map(Float::parseFloat).collect(Collectors.toList()));
            }
            Location repoLocation = InputConverter.convertString2RepoLocation(br.readLine());
            br.readLine();
            List<Float> repoDurationList = Arrays.stream(br.readLine().split(","))
                    .map(Float::parseFloat).collect(Collectors.toList());
            Duration duration = Duration.builder()
                    .orderNodeMatrix(orderNodeMatrix)
                    .vehicleMatrix(vehicleMatrix)
                    .repoList(repoDurationList)
                    .build();
            Cost cost = Cost.builder()
                    .travel(InputConverter.convertString2Cost(br.readLine()))
                    .waiting(InputConverter.convertString2Cost(br.readLine()))
                    .late(InputConverter.convertString2Cost(br.readLine()))
                    .build();
            return Input.builder()
                    .orders(orders)
                    .vehicles(vehicles)
                    .cost(cost)
                    .repoLocation(repoLocation)
                    .duration(duration)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
