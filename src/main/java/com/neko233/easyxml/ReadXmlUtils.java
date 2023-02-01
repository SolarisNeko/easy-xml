package com.neko233.easyxml;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Read XML from InputStream / other Stream
 *
 * @author SolarisNeko
 * Date on 2023-01-01
 */
public class ReadXmlUtils {


    static String readXmlFromInputStream(final Reader reader) throws IOException {
        final BufferedReader bufReader = toBufferedReader(reader);
        final List<String> lineList = new ArrayList<>();
        String line;
        while ((line = bufReader.readLine()) != null) {
            lineList.add(line);
        }
        return lineList.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }


    public static String readXmlFromInputStream(InputStream input, Charset charset) throws IOException {
        final InputStreamReader reader = new InputStreamReader(input, charset);
        return readXmlFromInputStream(reader);
    }
}
