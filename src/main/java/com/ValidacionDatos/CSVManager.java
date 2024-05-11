package com.ValidacionDatos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class CSVManager {

    private static FileWriter fileWriter;

    public CSVManager(String fileName) {
        String filePath = "src\\data\\" + fileName;

        try {
            fileWriter = new FileWriter(filePath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void turnArraytoData(ArrayList<String> data) {
        try {
            StringBuilder csvData = new StringBuilder();
            for (String value : data) {
                csvData.append(value).append(",");
            }
            csvData.deleteCharAt(csvData.length() - 1); // Remove the last comma
            fileWriter.write(csvData.toString() + "\n");
            fileWriter.flush(); // Flush the writer to ensure data is written immediately
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
