package com.example.amia.schoolrent.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailUtil {
    /**
     * 判断邮箱格式是否正确
     * @param address
     * @return
     */
    public static boolean isMail(String address){
        boolean result = false;
        try {
            String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(address);
            result = matcher.matches();
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
