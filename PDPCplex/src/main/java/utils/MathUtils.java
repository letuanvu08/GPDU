package utils;

public class MathUtils {
    public static int calFact(int n) {
        return n == 0 ? 1 : n * calFact(n - 1);
    }

    public static int calCombination(int k, int n) {
        return calFact(n) / calFact(k) * calFact(n - k);
    }
}
