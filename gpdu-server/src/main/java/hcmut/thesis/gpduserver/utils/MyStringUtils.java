package hcmut.thesis.gpduserver.utils;

import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class MyStringUtils {
    public static String removeUnicode(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        str = str.toLowerCase().trim();
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = temp.replaceAll("đ", "d");
        return pattern.matcher(temp).replaceAll("").trim();
    }
    public static String formatE164Phone(String phone) {
        String formattedPhone = phone;
        if (phone.charAt(0) == '0') {
            formattedPhone = formattedPhone.replaceFirst("0", "");
        }
        return "+84" + formattedPhone;
    }
}
