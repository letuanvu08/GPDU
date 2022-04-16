package hcmut.thesis.gpduserver.ai.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomKey {
    public static int generateInSize(int size) {
        return ThreadLocalRandom.current().nextInt(0, size);
    }

    public static int random(int origin, int size) {
        return ThreadLocalRandom.current().nextInt(origin, size);
    }


    public static int generateBaseMin(int min, int bound) {
        return ThreadLocalRandom.current().nextInt(min, min + bound);
    }
}
