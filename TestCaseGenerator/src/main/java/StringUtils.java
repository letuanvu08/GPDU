import models.Order;
import models.Vehicle;

import java.util.List;

public class StringUtils {
    public static String convertDurationMatrix2String(List<List<Integer>> durationMatrix) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < durationMatrix.size(); i++) {
            StringBuilder temp = new StringBuilder();
            for (int j = 0; j < durationMatrix.get(i).size(); j++) {
                temp.append(durationMatrix.get(i).get(j));
                if (j != durationMatrix.get(i).size() - 1) temp.append(",");
            }
            result.append(temp);
            result.append("\n");
        }
        return result.toString();
    }
}
