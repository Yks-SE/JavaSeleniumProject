package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderUtil {

    /**
     * Reads a CSV file and returns its data as a List of String arrays.
     *
     * @param filePath relative or absolute path to CSV file
     * @return List of rows, each row is a String[] of values
     */
    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true; // skip the first line (header)

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] values = line.split(",", -1); // split by comma
                data.add(values);
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to read CSV file: " + filePath, e);
        }

        return data;
    }
}