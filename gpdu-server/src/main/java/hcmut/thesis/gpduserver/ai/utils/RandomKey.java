package hcmut.thesis.gpduserver.ai.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomKey {
    public static int generateInSize(int size) {
        return ThreadLocalRandom.current().nextInt(0, size);
    }


    public static int generateBaseMin(int min, int bound) {
        return ThreadLocalRandom.current().nextInt(min, 1000);
    }
}
