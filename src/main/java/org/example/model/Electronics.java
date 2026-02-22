package org.example.model;

import java.util.Optional;

public class Electronics extends Product {
    private double weight;

    // Constructor
    public Electronics(String category, String prodName, Integer quantity,
                       double unitCost, Integer margin, double weight) {
        super(category, prodName, quantity, unitCost, margin);
        this.weight = weight;
    }

    // Getter
    public double getWeight() { return weight; }

    // Create an Electronics object, returning an Optional containing the object only if related parameters are valid
    public static Optional<Electronics> create (String category, String prodName, Integer quantity,
                                                double unitCost, Integer margin, double weight) {
        if (category.matches("\\d+")) return Optional.empty();
        if (prodName == null || prodName.isBlank() || prodName.matches("\\d+")) return Optional.empty();
        if (quantity < 0) return Optional.empty();
        if (unitCost < 0) return Optional.empty();
        if (margin < 0) return Optional.empty();
        if (weight < 0) return Optional.empty();
        return Optional.of(new Electronics(category, prodName, quantity, unitCost, margin, weight));
    }

    @Override
    public double getShippingCost(double costPerMeasure, Integer quantity) {
        return getWeight() * costPerMeasure * getQuantity(); // scale by quantity
    }

    @Override
    public String toString () {
        return getCategory() + " | " + getProdName() + " | " + getQuantity()
                + " | " + getUnitCost() + " | " + getMargin() + " | " + getWeight();
    }
}




