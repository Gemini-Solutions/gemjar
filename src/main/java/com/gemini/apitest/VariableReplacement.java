package com.gemini.apitest;

import com.gemini.generic.QuanticAPIBase;
import com.gemini.generic.QuanticGlobalVar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class VariableReplacement {


    public static JsonElement result(JsonElement obj){
        return getResultantJson(obj);
    }

    public static JsonElement getResultantJson(JsonElement job) {
        String var2 = job.toString();
        char search = '#';             // Character to search is '#'.
        int count = 0;
        for (int i = 0; i < var2.length(); i++) {
            if (var2.charAt(i) == search)
                count++;
        }
        int number = count / 2;
        for (int k = 0; k < number; k++) {
            String s = getResultantString(var2);
            var2 = s;
        }
        JsonElement temp1 = (JsonElement) JsonParser.parseString(var2);
        return temp1;
    }

    public static String getResultantString(String var2) {
        int first = var2.indexOf("#");
        int second = var2.indexOf("#", first + 1);
        String start = var2.substring(0, first);
        String end = var2.substring(second + 1);
        String buffer = var2.substring(first + 1, second);
        if (buffer.contains("test_response"))
        {
            String t = Jenkins.Replace("#"+buffer+"#", QuanticGlobalVar.globalResponseHM);
            return start+t+end;
        }
        else {
            String[] arrays = buffer.split("-", 3);
            String user = arrays[0];
            String up = user.toUpperCase();
            if (up.contains("UNIQUE")) {
                int n = Integer.parseInt(arrays[1]);
                if (n < 10) {
                    int number = getRandomNumber(n);
                    var2 = start + number + end;
                    return var2;
                } else {
                    long currentTimestamp = System.currentTimeMillis();
                    Long number = getLongNumber(currentTimestamp, n);
                    var2 = start + number + end;
                    return var2;
                }
            } else if (up.contains("CURR")) {
                String temp3 = arrays[1];
                String p = dateTime(temp3, start, end);
                return p;
            } else if (up.contains("ALPHA")) {
                int n = Integer.parseInt(arrays[1]);
                String temp4 = getAlphaNumericString(n);
                return start + temp4 + end;
            } else if (up.contains("EPOCH")) {
                int n = 20;
                long currentTimestamp = System.currentTimeMillis();
                Long number = getLongNumber(currentTimestamp, n);
                var2 = start + number + end;
                return var2;
            } else if ((up.contains("UNQ"))) {
                String w = arrays[1];
                String[] ar = w.split("_", 2);
                int firstpart = Integer.parseInt(ar[0].toString());
                int secondpart = Integer.parseInt(ar[1].toString());

                int po = xToY(firstpart, secondpart);
                return start + po + end;
            }
        }
        return null;
    }
    public static int xToY(int min,int max){
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        return random_int;}

    public static int getRandomNumber(int n) {
        int m = (int) Math.pow(10, n - 1);
        return m + new Random().nextInt(9 * m);
    }

    public static long getLongNumber(long num, int n) {
        long first = (long) (num / Math.pow(10, Math.floor(Math.log10(num)) - n + 1));
        return first;
    }

    public static String dateTime(String a, String firstpart, String lastpsrt) {
        DateFormat dateFormat = new SimpleDateFormat(a);
        Date dates = new Date();
        String s = dateFormat.format(dates);
        return firstpart + s + lastpsrt;
    }

    static String getAlphaNumericString(int n) {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());
            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}