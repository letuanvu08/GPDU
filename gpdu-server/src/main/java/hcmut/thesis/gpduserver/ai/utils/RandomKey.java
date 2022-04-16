package hcmut.thesis.gpduserver.ai.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomKey {
    public static int generateVehicleRandom(int size) {
        return ThreadLocalRandom.current().nextInt(1, size + 1) * 1000 +
                ThreadLocalRandom.current().nextInt(100, 1000);
    }

    public static int generateVehicleRandom(int size, int min) {
        return ThreadLocalRandom.current().nextInt(1, size + 1) * 1000 +
                ThreadLocalRandom.current().nextInt(min + 1, 1000);
    }

    public static int generate(int vehicleNumber) {
        return vehicleNumber * 1000 +
                ThreadLocalRandom.current().nextInt(100, 1000);
    }

    public static int generate(int vehicleNumber, int min) {
        return vehicleNumber * 1000 +
                ThreadLocalRandom.current().nextInt(100, 1000);
    }
}
