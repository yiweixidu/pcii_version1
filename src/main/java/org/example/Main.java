package org.example;

import org.example.dao.ClothingDAO;
import org.example.dao.ElectronicsDAO;
import org.example.io.CSVProductReader;
import org.example.model.Product;
import org.example.model.Electronics;
import org.example.model.Clothing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        List<Electronics> electronicsCSVList = new ArrayList<>();
        List<Clothing> clothingCSVList = new ArrayList<>();
        ElectronicsDAO electronicsDAO = new ElectronicsDAO();
        List<Electronics> dbElectronics = electronicsDAO.getAllElectronics();
        ClothingDAO clothingDAO = new ClothingDAO();
        List<Clothing> dbClothing = clothingDAO.getAllClothing();

        boolean exit = false;

        while (!exit) {
            // Menu
            System.out.println("\n==== Inventory System ====");
            System.out.println("1. Import Electronics CSV");
            System.out.println("2. Import Clothing CSV");
            System.out.println("3. Display Electronics (from CSV)");
            System.out.println("4. Display Clothing (from CSV)");
            System.out.println("5. Insert Electronics to DB (from CSV)");
            System.out.println("6. Insert Clothing to DB (from CSV)");
            System.out.println("7. Display Electronics (from DB)");
            System.out.println("8. Display Clothing (from DB)");
            System.out.println("9. Compute Total Inventory Value (from imported CSV/DB)");
            System.out.println("10. Display Shipping Costs (from DB)");
            System.out.println("0. Exit");

            System.out.print("Select option: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number 1-7.");
                continue;
            }

            switch (option) {
                case 1:
                    try {
                        List<Electronics> imported = CSVProductReader.readElectronics("./src/main/resources/Electronics.csv");
                        electronicsCSVList.clear();
                        electronicsCSVList.addAll(imported);
                        System.out.println("Imported " + imported.size() + " electronics products.");
                    } catch (IOException e) {
                        System.out.println("Error reading electronics CSV: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        List<Clothing> imported = CSVProductReader.readClothing("./src/main/resources/Clothing.csv");
                        clothingCSVList.clear();
                        clothingCSVList.addAll(imported);
                        System.out.println("Imported " + imported.size() + " clothing products.");
                    } catch (IOException e) {
                        System.out.println("Error reading clothing CSV: " + e.getMessage());
                    }
                    break;

                case 3:
                    if (electronicsCSVList.isEmpty()) {
                        System.out.println("No electronics products imported yet.");
                    } else {
                        System.out.println("\n--- Electronics Products ---");
                        displayCSVElectronics(electronicsCSVList);
                    }
                    break;

                case 4:
                    if (clothingCSVList.isEmpty()) {
                        System.out.println("No clothing products imported yet.");
                    } else {
                        System.out.println("\n--- Clothing Products ---");
                        displayCSVClothing(clothingCSVList);
                    }
                    break;

                case 5:
                    if (electronicsCSVList.isEmpty()) {
                        System.out.println("Please import electronics CSV first (option 1).");
                    } else {
                        try {
                            electronicsDAO.batchInsertElectronics(electronicsCSVList);
                            System.out.println("Electronics inserted into database successfully.");
                            dbElectronics = electronicsDAO.getAllElectronics();
                        } catch (SQLException e) {
                            System.out.println("Error inserting electronics: " + e.getMessage());
                        }
                    }
                    break;

                case 6:
                    if (clothingCSVList.isEmpty()) {
                        System.out.println("Please import clothing CSV first (option 2).");
                    } else {
                        try {
                            clothingDAO.batchInsertClothing(clothingCSVList);
                            System.out.println("Clothing inserted into database successfully.");
                            dbClothing = clothingDAO.getAllClothing();
                        } catch (SQLException e) {
                            System.out.println("Error inserting clothing: " + e.getMessage());
                        }
                    }
                    break;

                case 7:
                    displayElectronics();
                    break;

                case 8:
                    displayClothing();
                    break;

                case 9:
                    // Compute total value inserted into database from CSV file
                    double totalValue = 0.0;
                    for (Electronics e : electronicsCSVList) totalValue += e.getInventoryValue();
                    for (Clothing c : clothingCSVList) totalValue += c.getInventoryValue();
                    System.out.printf("Total inventory value from imported CSV: %.2f%n", totalValue);

                    // Compute total value of inventory database
                    double totalValueFromDB = 0.0;
                    for (Electronics e : dbElectronics) totalValueFromDB += e.getInventoryValue();
                    for (Clothing c : dbClothing) totalValueFromDB += c.getInventoryValue();
                    System.out.printf("Total inventory value from database: %.2f%n", totalValueFromDB);
                    break;

                case 10:
                    if (!dbElectronics.isEmpty()) {
                        System.out.print("Enter shipping cost per kg for Electronics: ");
                        double costPerKg;
                        try {
                            costPerKg = Double.parseDouble(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input, defaulting to 2.0 per kg");
                            costPerKg = 2.0;
                        }
                        displayShippingCosts("Electronics", dbElectronics, costPerKg);
                    }

                    if (!dbClothing.isEmpty()) {
                        System.out.print("Enter shipping cost per cubic center meter for Clothing: ");
                        double costPerCM3;
                        try {
                            costPerCM3 = Double.parseDouble(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input, defaulting to 0.0015 per cm³");
                            costPerCM3 = 0.00015;
                        }
                        displayShippingCosts("Clothing", dbClothing, costPerCM3);
                    }
                    break;

                case 0:
                    System.out.println("Exiting Inventory System. Goodbye!");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option, please enter a number 1-7.");
            }
        }

        scanner.close();
    }

    public static void displayCSVElectronics(List<Electronics> electronicsList) {
        System.out.printf("%-15s %-25s %-10s %-15s %-15s %-15s %-10s\n",
                "Category", "Product Name", "Unit Cost", "Margin(%)", "Quantity", "Unit Price", "Weight(kg)");
        System.out.println("-".repeat(115));
        for (Electronics electro: electronicsList) {
            System.out.printf("%-15s %-25s %-10.2f %-15d %-15d %-15.2f %-10.2f\n",
                    electro.getCategory(), electro.getProdName(), electro.getUnitCost(),
                    electro.getMargin(), electro.getQuantity(), electro.getUnitPrice(), electro.getWeight());
        }
    }

    public static void displayCSVClothing(List<Clothing> clothingList) {
        System.out.printf("%-20s %-30s %-10s %-15s %-15s %-15s %-10s\n",
                "Category", "Product Name", "Unit Cost", "Margin(%)", "Quantity", "Unit Price", "Volume(cm³)");
        System.out.println("-".repeat(125));
        for (Clothing clothing : clothingList) {
            System.out.printf("%-20s %-30s %-10.2f %-15d %-15d %-15.2f %-10.2f\n",
                    clothing.getCategory(), clothing.getProdName(), clothing.getUnitCost(),
                    clothing.getMargin(), clothing.getQuantity(), clothing.getUnitPrice(), clothing.getVolume());
        }
    }

    public static void displayElectronics() {
        ElectronicsDAO electronicsDAO = new ElectronicsDAO();
        List<Electronics> electronicsList = electronicsDAO.getAllElectronics();
        System.out.printf("%-15s %-25s %-10s %-15s %-15s %-15s %-10s\n",
                "Category", "Product Name", "Unit Cost", "Margin(%)", "Quantity", "Unit Price", "Weight(kg)");
        System.out.println("-".repeat(115));
        for (Electronics electro: electronicsList) {
            System.out.printf("%-15s %-25s %-10.2f %-15d %-15d %-15.2f %-10.2f\n",
                    electro.getCategory(), electro.getProdName(), electro.getUnitCost(),
                    electro.getMargin(), electro.getQuantity(), electro.getUnitPrice(), electro.getWeight());
        }
    }

    public static void displayClothing() {
        ClothingDAO clothingDAO = new ClothingDAO();
        List<Clothing> clothingList = clothingDAO.getAllClothing();
        System.out.printf("%-20s %-30s %-10s %-15s %-15s %-15s %-10s\n",
                "Category", "Product Name", "Unit Cost", "Margin(%)", "Quantity", "Unit Price", "Volume(cm³)");
        System.out.println("-".repeat(125));
        for (Clothing clothing : clothingList) {
            System.out.printf("%-20s %-30s %-10.2f %-15d %-15d %-15.2f %-10.2f\n",
                    clothing.getCategory(), clothing.getProdName(), clothing.getUnitCost(),
                    clothing.getMargin(), clothing.getQuantity(), clothing.getUnitPrice(), clothing.getVolume());
        }
    }

    public static void displayShippingCosts(String category, List<? extends Product> products, double costPerUnit) {
        System.out.println("\n--- " + category + " Shipping Costs ---");
        double totalShippingCost = 0.0;

        for (Product p : products) {
            double shippingCost = p.getShippingCost(costPerUnit, p.getQuantity());
            System.out.printf("%-25s %8d units: %10.2f shipping cost\n",
                    p.getProdName(), p.getQuantity(), shippingCost);
            totalShippingCost += shippingCost;
        }

        System.out.printf("Total %s shipping cost: %.2f\n", category, totalShippingCost);
    }
}