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

    // Use BufferedReader and Files.newBufferedReader for efficient reading.
    public static List<Electronics> readElectronics(String csvFile) throws IOException {
        List<Electronics> electronicsList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFile))) {
            String line;
            boolean isFirstLine = true;     // Skip the first line (header) with isFirstLine flag.
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;    // Mark that we've seen the FirstLine
                    continue;               // Skip processing for the header line and move to the next line
                }
                String[] parts = line.split(",");   // Split into parts with line.split(",")
                if (parts.length < 6) {     // Check parts.length < 6 to skip malformed lines.
                    continue;
                }
                try {                       // Constructor calls for Electronics with the parsed values.
                    String category = parts[0].trim();
                    String prodName = parts[1].trim();
                    Integer quantity = Integer.parseInt(parts[2].trim());
                    double unitCost = Double.parseDouble(parts[3].trim());
                    Integer margin = Integer.parseInt(parts[4].trim());
                    double weight = Double.parseDouble(parts[5].trim());
                    Electronics.create(category, prodName, quantity, unitCost, margin, weight)
                            .ifPresent(electronicsList::add);      // add only if valid
                } catch (NumberFormatException ignored) {        // Catch NumberFormatException silently (ignoring errors).
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
                    double volume = Double.parseDouble(parts[5].trim());
                    Clothing.create(category, prodName, quantity, unitCost, margin, volume)
                            .ifPresent(clothingList::add);      // add only if valid
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return clothingList;
    }
}