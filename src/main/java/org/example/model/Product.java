package org.example.model;

public abstract class Product {
    private String category;
    private String prodName;
    private int quantity;
    private double unitCost;
    private Integer margin;

    // Constructor
    public Product(String category, String prodName, Integer quantity, double unitCost, Integer margin) {
        this.category = category;
        this.prodName = prodName;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.margin = margin;
    }

    // Getters
    public String getCategory() { return category; }
    public String getProdName() { return prodName; }
    public int getQuantity() { return quantity; }
    public double getUnitCost() { return unitCost; }
    public Integer getMargin() { return margin; }

    // Compute inventory value
    public double getInventoryValue() { return unitCost * quantity; }

    // Compute unitPrice
    public double getUnitPrice() { return unitCost * (1 + margin / 100.0); }

    // Abstract method for shipping cost
    public abstract double getShippingCost(double measure, Integer quantity);

    @Override
    public String toString() {
        return category + " | " + prodName + " | " + quantity + " | " + unitCost + " | " + margin;
    }
}
