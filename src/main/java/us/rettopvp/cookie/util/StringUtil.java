package us.rettopvp.cookie.util;

import java.util.Random;

public class StringUtil {
	
    private static char[] teamSort;
    private static char[] colorCodes;
    private Random random;
    
    static {
        StringUtil.teamSort = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
        StringUtil.colorCodes = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'n', 'o', 'r' };
    }
    
    public StringUtil() {
        this.random = new Random();
    }
    
    public String getAlphabeticalString(double index) {
        String toReturn = "";
        double wheelAmount = index / 26.0;
        for (int i = 0; i < Math.floor(wheelAmount); ++i) {
            toReturn += StringUtil.teamSort[25];
        }
        toReturn += StringUtil.teamSort[(int)Math.round(26.0 * (wheelAmount - Math.floor(wheelAmount)))];
        return toReturn;
    }
    
    public String[] splitString(String string) {
        String[] strings = new String[3];
        if (string.length() <= 16) {
            strings[0] = string;
            strings[2] = (strings[1] = "");
        }
        else if (string.length() <= 32) {
            strings[0] = string.substring(0, 15);
            strings[1] = string.substring(15, string.length());
            strings[2] = "";
        }
        else if (string.length() <= 48) {
            strings[0] = string.substring(0, 15);
            strings[1] = string.substring(15, 31);
            strings[2] = string.substring(31, string.length());
        } else {
            strings[0] = string.substring(0, 15);
            strings[1] = string.substring(15, 31);
            strings[2] = string.substring(31, 47);
        }
        if (strings[1].length() <= 0) {
            strings[1] = this.randomColorString();
        }
        return strings;
    }
    
    public String fillEndString(String s) {
        if (s.length() >= 16) {
            return s;
        }
        return s + this.randomColorIndex(this.nearestMultiple(2, 16 - s.length()));
    }
    
    public String randomColorString() {
        String toReturn = "";
        for (int i = 0; i < 7; ++i) {
            toReturn = toReturn + "??" + StringUtil.colorCodes[this.random.nextInt(StringUtil.colorCodes.length)];
        }
        toReturn += "??r";
        return toReturn;
    }
    
    private String randomColorIndex(int index) {
        String toReturn = "";
        for (int i = 0; i < (index - 2) / 2; ++i) {
            toReturn = toReturn + "??" + StringUtil.colorCodes[this.random.nextInt(StringUtil.colorCodes.length)];
        }
        if (toReturn.length() <= 14) {
            toReturn += "??r";
        }
        return toReturn;
    }
    
    private int nearestMultiple(int x, int v) {
        return (int)(x * Math.floor(Math.abs(v / x)));
    }
}
