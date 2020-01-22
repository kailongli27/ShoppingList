package com.example.shoppinglist.model;

public class Item {
    private String name;
    private int quantity;
    private String dateAdded;
    private int id;

    public Item(){}

    public Item(String name, int quantity, String dateAdded, int id) {
        this.name = name;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
        this.id = id;
    }

    public Item(String name, int quantity, String dateAdded) {
        this.name = name;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
            this.quantity = quantity;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
