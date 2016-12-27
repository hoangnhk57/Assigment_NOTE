package com.example.jax.Note.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jax on 24-Dec-16.
 */

public class PathImageUtility {
    private static String LIST_SEPARATOR = "__,__";

    public static String convertListToString(List<String> stringList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : stringList) {
            stringBuffer.append(str).append(LIST_SEPARATOR);
        }
        // Remove last separator
     //   int lastIndex = stringBuffer.lastIndexOf(LIST_SEPARATOR);
       // stringBuffer.delete(lastIndex, lastIndex + LIST_SEPARATOR.length() + 1);
        return stringBuffer.toString();
    }

    public static List<String> convertStringToList(String str) {
        return Arrays.asList(str.split(LIST_SEPARATOR));
    }
}
