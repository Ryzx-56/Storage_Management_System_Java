package com.mycompany.java2project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ManageProducts extends JFrame implements AppearanceSettings.AppearanceListener {
    private ProductInventory inventory;
    private JPanel productPanel;
    private JComboBox<String> deleteComboBox;
    private JPanel buttonPanel;

    public ManageProducts(ProductInventory inventory) {
        super("Manage Products");
        this.inventory = inventory;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main product panel using GridBagLayout
        productPanel = new JPanel(new GridBagLayout());
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel for buttons
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Add Product button
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> addNewProduct());

        // Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetQuantities());

        // Delete Product components
        deleteComboBox = new JComboBox<>();
        updateDeleteComboBox();

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> deleteSelectedProduct());

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());

        // Add components to the button panel
        buttonPanel.add(deleteComboBox);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        add(new JScrollPane(productPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshProductPanel();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Register this frame as a listener
        AppearanceSettings.getInstance().addListener(this);

        // Set initial appearance
        updateAppearance(AppearanceSettings.getInstance().getCurrentColor());
    }

    private void addNewProduct() {
        String[] options = {"Clothes", "Electronic Device", "Food", "Furniture", "Tools"};
        String category = (String) JOptionPane.showInputDialog(
                this, "Select a category:", "Add Product",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]
        );
        if (category != null) {
            String productName = JOptionPane.showInputDialog("Enter the name of the product:");
            if (productName != null && !productName.trim().isEmpty()) {
                inventory.addProduct(productName, category, 5);
                refreshProductPanel();
                updateDeleteComboBox();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid product name.");
            }
        }
    }

    private void deleteSelectedProduct() {
        String selectedProduct = (String) deleteComboBox.getSelectedItem();
        if (selectedProduct != null) {
            int indexToDelete = -1;
            for (int i = 0; i < inventory.getProducts().size(); i++) {
                String productName = inventory.getCategories().get(i) + " - " + inventory.getProducts().get(i);
                if (productName.equals(selectedProduct)) {
                    indexToDelete = i;
                    break;
                }
            }

            if (indexToDelete != -1) {
                inventory.getProducts().remove(indexToDelete);
                inventory.getCategories().remove(indexToDelete);
                inventory.getQuantities().remove(indexToDelete);
                refreshProductPanel();
                updateDeleteComboBox();
            }
        }
    }

    private void refreshProductPanel() {
        productPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < inventory.getProducts().size(); i++) {
            addProductRow(inventory.getProducts().get(i), inventory.getCategories().get(i), inventory.getQuantities().get(i), i, gbc);
        }
        productPanel.revalidate();
        productPanel.repaint();
    }

    private void addProductRow(String productName, String category, int quantity, int index, GridBagConstraints gbc) {
        // Product info label
        JLabel productLabel = new JLabel(category + " - " + productName);
        gbc.gridx = 0;
        gbc.gridy = index;
        productPanel.add(productLabel, gbc);

        // Quantity label
        JLabel quantityLabel = new JLabel(String.valueOf(quantity));
        gbc.gridx = 1;
        productPanel.add(quantityLabel, gbc);

        // Increment button
        JButton incrementButton = new JButton("+");
        incrementButton.addActionListener(e -> {
            int newQuantity = inventory.getQuantities().get(index) + 1;
            inventory.updateQuantity(index, newQuantity);
            quantityLabel.setText(Integer.toString(newQuantity));
        });
        gbc.gridx = 2;
        productPanel.add(incrementButton, gbc);

        // Decrement button
        JButton decrementButton = new JButton("-");
        decrementButton.addActionListener(e -> {
            int currentQuantity = inventory.getQuantities().get(index);
            if (currentQuantity > 0) {
                int newQuantity = currentQuantity - 1;
                inventory.updateQuantity(index, newQuantity);
                quantityLabel.setText(Integer.toString(newQuantity));
            }
        });
        gbc.gridx = 3;
        productPanel.add(decrementButton, gbc);
    }

    private void resetQuantities() {
        for (int i = 0; i < inventory.getQuantities().size(); i++) {
            inventory.updateQuantity(i, 5);
        }
        refreshProductPanel();
        JOptionPane.showMessageDialog(this, "Quantities have been reset to their original values.");
    }

    private void updateDeleteComboBox() {
        deleteComboBox.removeAllItems();
        for (int i = 0; i < inventory.getProducts().size(); i++) {
            String productName = inventory.getCategories().get(i) + " - " + inventory.getProducts().get(i);
            deleteComboBox.addItem(productName);
        }
    }

    @Override
    public void updateAppearance(Color newColor) {
        getContentPane().setBackground(newColor);
        productPanel.setBackground(newColor);
        buttonPanel.setBackground(newColor);

        // Update components inside productPanel
        Component[] components = productPanel.getComponents();
        for (Component component : components) {
            component.setBackground(newColor);
            if (component instanceof JLabel || component instanceof JButton) {
                ((JComponent) component).setForeground(getContrastColor(newColor));
            }
        }

        // Update components inside buttonPanel
        Component[] buttonComponents = buttonPanel.getComponents();
        for (Component component : buttonComponents) {
            component.setBackground(newColor);
            if (component instanceof JButton || component instanceof JComboBox) {
                ((JComponent) component).setForeground(getContrastColor(newColor));
            }
        }
    }

    // Method to get a contrasting text color
private Color getContrastColor(Color color) {
    int brightness = (int) Math.sqrt(
        color.getRed() * color.getRed() * .241 +
        color.getGreen() * color.getGreen() * .691 +
        color.getBlue() * color.getBlue() * .068);
    return brightness < 130 ? Color.WHITE : Color.BLACK;

    }
}