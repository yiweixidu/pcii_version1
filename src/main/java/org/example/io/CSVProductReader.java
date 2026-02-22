package org.example.io;

import org.example.model.Clothing;
import org.example.model.Electronics;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVProductReader {

    public static List<Electronics> readElectronics(String csvFile) throws IOException {
        List<Electronics> electronicsList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFile))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    continue;
                }
                try {
                    String category = parts[0].trim();
                    String prodName = parts[1].trim();
                    Integer quantity = Integer.parseInt(parts[2].trim());
                    double unitCost = Double.parseDouble(parts[3].trim());
                    Integer margin = Integer.parseInt(parts[4].trim());
                    double weight = Double.parseDouble(parts[5].trim());
                    Electronics electronics = new Electronics(category, prodName, quantity, unitCost, margin, weight);
                    electronicsList.add(electronics);
                } catch (NumberFormatException e) {
                }
            }
        }
        return electronicsList;
    }

    public static List<Clothing> readClothing(String csvFile) throws IOException {
        List<Clothing> clothingList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFile))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    continue;
                }
                try {
                    String category = parts[0].trim();
                    String prodName = parts[1].trim();
                    Integer quantity = Integer.parseInt(parts[2].trim());
                    double unitCost = Double.parseDouble(parts[3].trim());
                    Integer margin = Integer.parseInt(parts[4].trim());
                    double volume = Double.parseDouble(parts[5].trim()); // 注意此处使用的是volume
                    Clothing clothing = new Clothing(category, prodName, quantity, unitCost, margin, volume);
                    clothingList.add(clothing);
                } catch (NumberFormatException e) {
                }
            }
        }
        return clothingList;
    }
}