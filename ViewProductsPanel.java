package com.mycompany.java2project2;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ViewProductsPanel extends JPanel implements AppearanceSettings.AppearanceListener {
    private ProductInventory inventory;
    private JPanel productGrid;

    // Product name to image path mapping
    private Map<String, String> productImageMap = new HashMap<>();

    public ViewProductsPanel(ProductInventory inventory) {
        this.inventory = inventory;

        // Set up the mapping of product names to image paths
        initializeProductImages();

        setLayout(new BorderLayout());

        productGrid = new JPanel(new GridLayout(0, 3, 10, 10));

        // Add a listener to refresh the panel when the inventory changes
        inventory.addListener(this::refreshProductGrid);
        refreshProductGrid();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (parentFrame != null) {
                parentFrame.dispose();
            }
        });

        add(new JLabel("Product Inventory", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(productGrid), BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Register this panel as a listener
        AppearanceSettings.getInstance().addListener(this);

        // Set initial appearance
        updateAppearance(AppearanceSettings.getInstance().getCurrentColor());
    }

    private void initializeProductImages() {
        // Define products and their corresponding image paths
        String[] specificProducts = {"Shirt", "Apple", "Sofa", "Hammer", "Laptop"};
        String[] productImages = {
            "C:\\Users\\User6\\Documents\\Uni\\Major y1\\Sim1\\Java II\\G project\\tshirt2.png", 
            "C:\\Users\\User6\\Documents\\Uni\\Major y1\\Sim1\\Java II\\G project\\apple.png",    
            "C:\\Users\\User6\\Documents\\Uni\\Major y1\\Sim1\\Java II\\G project\\sofa.png",     
            "C:\\Users\\User6\\Documents\\Uni\\Major y1\\Sim1\\Java II\\G project\\hammer.png",
            "C:\\Users\\User6\\Documents\\Uni\\Major y1\\Sim1\\Java II\\G project\\laptop.png",
        };

        // Map each specific product to its image path
        for (int i = 0; i < specificProducts.length; i++) {
            productImageMap.put(specificProducts[i].toLowerCase(), productImages[i]);
        }
    }

    private void refreshProductGrid() {
        productGrid.removeAll();

        for (int i = 0; i < inventory.getProducts().size(); i++) {
            JPanel productPanel = new JPanel();
            productPanel.setLayout(new BorderLayout());
            productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Product category label at the top
            JLabel categoryLabel = new JLabel(inventory.getCategories().get(i), SwingConstants.CENTER);
            categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            productPanel.add(categoryLabel, BorderLayout.NORTH);

            // Product image
            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(100, 100));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            String productName = inventory.getProducts().get(i);
            String imagePath = productImageMap.get(productName.toLowerCase());

            // Load the product image if it exists
            if (imagePath != null) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                // Placeholder if no image is found
                imageLabel.setText("No Image");
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
            productPanel.add(imageLabel, BorderLayout.CENTER);

            // Product name and quantity
            JLabel nameLabel = new JLabel(productName, SwingConstants.CENTER);
            JLabel quantityLabel = new JLabel("Quantity: " + inventory.getQuantities().get(i), SwingConstants.CENTER);

            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.add(nameLabel);
            infoPanel.add(quantityLabel);
            productPanel.add(infoPanel, BorderLayout.SOUTH);

            productGrid.add(productPanel);
        }
        productGrid.revalidate();
        productGrid.repaint();
    }

    @Override
    public void updateAppearance(Color newColor) {
        setBackground(newColor);
        productGrid.setBackground(newColor);

        // Update product panels
        for (Component comp : productGrid.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel productPanel = (JPanel) comp;
                productPanel.setBackground(newColor);

                for (Component innerComp : productPanel.getComponents()) {
                    if (innerComp instanceof JLabel) {
                        ((JLabel) innerComp).setForeground(getContrastColor(newColor));
                    } else if (innerComp instanceof JPanel) {
                        innerComp.setBackground(newColor);
                        for (Component infoComp : ((JPanel) innerComp).getComponents()) {
                            if (infoComp instanceof JLabel) {
                                ((JLabel) infoComp).setForeground(getContrastColor(newColor));
                            }
                        }
                    }
                }
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