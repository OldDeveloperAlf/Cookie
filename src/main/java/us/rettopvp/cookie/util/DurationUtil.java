package us.rettopvp.cookie.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationUtil {
	
    private long value;
    
    public DurationUtil(long value) {
        this.value = value;
    }
    
    public static DurationUtil fromString(String source) {
        if (source.equalsIgnoreCase("perm") || source.equalsIgnoreCase("permanent")) {
            return new DurationUtil(2147483647L);
        }
        long totalTime = 0L;
        boolean found = false;
        Matcher matcher = Pattern.compile("\\d+\\D+").matcher(source);
        while (matcher.find()) {
            String s = matcher.group();
            Long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            String s2 = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
            switch (s2) {
                case "s": {
                    totalTime += value;
                    found = true;
                    continue;
                }
                case "m": {
                    totalTime += value * 60L;
                    found = true;
                    continue;
                }
                case "h": {
                    totalTime += value * 60L * 60L;
                    found = true;
                    continue;
                }
                case "d": {
                    totalTime += value * 60L * 60L * 24L;
                    found = true;
                    continue;
                }
                case "w": {
                    totalTime += value * 60L * 60L * 24L * 7L;
                    found = true;
                    continue;
                }
                case "M": {
                    totalTime += value * 60L * 60L * 24L * 30L;
                    found = true;
                    continue;
                }
                case "y": {
                    totalTime += value * 60L * 60L * 24L * 365L;
                    found = true;
                    continue;
                }
            }
        }
        return new DurationUtil(found ? (totalTime * 1000L) : -1L);
    }
    
    public long getValue() {
        return this.value;
    }
}
