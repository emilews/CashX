package org.bitcoinj.utils;

import java.io.IOException;
import java.io.Reader;

public class JSONHelper {
    public static String readJSONFile(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
