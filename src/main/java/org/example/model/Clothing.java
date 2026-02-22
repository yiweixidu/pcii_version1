package org.example.model;

import java.util.Optional;

public class Clothing extends Product {
    private double volume;

    // Constructor
    public Clothing(String category, String prodName, Integer quantity, double unitCost, Integer margin, double volume) {
        super(category, prodName, quantity, unitCost, margin);
        this.volume = volume;
    }

    // Getter
    public double getVolume() { return volume; }

    // Create a Clothing object, returning an Optional containing the object only if related parameters are valid
    public static Optional<Clothing> create (String category, String prodName, Integer quantity,
                                                double unitCost, Integer margin, double volume) {
        if (category.matches("\\d+")) return Optional.empty();
        if (prodName == null || prodName.isBlank() || prodName.matches("\\d+")) return Optional.empty();
        if (quantity < 0) return Optional.empty();
        if (unitCost < 0) return Optional.empty();
        if (margin < 0) return Optional.empty();
        if (volume < 0) return Optional.empty();
        return Optional.of(new Clothing(category, prodName, quantity, unitCost, margin, volume));
    }

    @Override
    public double getShippingCost(double costPerMeasure, Integer quantity) {
        return getVolume() * costPerMeasure * getQuantity(); // scale by quantity
    }

    @Override
    public String toString () {
        return getCategory() + " | " + getProdName() + " | " + getQuantity()
                + " | " + getUnitCost() + " | " + getMargin() + " | " + getVolume();
    }
}
