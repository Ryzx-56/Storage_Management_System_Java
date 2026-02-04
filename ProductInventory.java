package com.mycompany.java2project2;

import java.util.ArrayList;
import java.util.List;

public class ProductInventory {
    private ArrayList<String> products = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<Integer> quantities = new ArrayList<>();
    private List<Runnable> listeners = new ArrayList<>();

    public ProductInventory() {
        // Initialize with default products
        String[] defaultProducts = {"Shirt", "Laptop", "Apple", "Sofa", "Hammer"};
        String[] defaultCategories = {"Clothes", "Electronic Device", "Food", "Furniture", "Tools"};

        for (int i = 0; i < defaultProducts.length; i++) {
            products.add(defaultProducts[i]);
            categories.add(defaultCategories[i]);
            quantities.add(5); // Default quantity
        }
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<Integer> getQuantities() {
        return quantities;
    }

    public void addProduct(String product, String category, int quantity) {
        products.add(product);
        categories.add(category);
        quantities.add(quantity);
        notifyListeners();
    }

    public void updateQuantity(int index, int quantity) {
        if (index >= 0 && index < quantities.size()) {
            quantities.set(index, quantity);
            notifyListeners();
        }
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
}