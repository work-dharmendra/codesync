package com.codesync.inttest.common;

import java.io.BufferedReader;
import java.io.IOException;

public class Util {

    public static String getPostBody(BufferedReader reader) {
        StringBuilder buffer = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();

    }
}
