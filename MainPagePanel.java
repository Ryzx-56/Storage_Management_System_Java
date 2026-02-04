package com.mycompany.java2project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPagePanel extends JPanel implements AppearanceSettings.AppearanceListener {
    private JButton ManageProduct = new JButton();
    private JButton ViewProduct = new JButton();
    private JButton Appearance = new JButton();
    public JLabel AppName;
    public JPanel appearanceBtn;
    public JPanel buttonPanel;
    public JPanel wrapperPanel;
    public JPanel middlepanel;
    private ProductInventory inventory = new ProductInventory(); // Shared inventory

    public MainPagePanel() {
        ManageProduct = new JButton("Manage Products");
        ViewProduct = new JButton("View Products");
        Appearance = new JButton("Change Appearance");
        AppName = new JLabel("Storage Management App", SwingConstants.CENTER);
        ImageIcon imageIcon = new ImageIcon("WP150.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        setLayout(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(ManageProduct);
        buttonPanel.add(ViewProduct);

        appearanceBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 20));

        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(buttonPanel, BorderLayout.CENTER);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));
        AppName.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        appearanceBtn.add(Appearance);
        middlepanel = new JPanel(new BorderLayout());
        middlepanel.add(imageLabel, BorderLayout.CENTER);
        middlepanel.add(wrapperPanel, BorderLayout.SOUTH);

        appearanceBtn.add(Appearance);
        add(AppName, BorderLayout.NORTH);
        add(middlepanel, BorderLayout.CENTER);
        add(appearanceBtn, BorderLayout.SOUTH);

        // Add action listeners
        Appearance.addActionListener(new AppearanceACTLIST());
        ManageProduct.addActionListener(new ManageACTLIST());
        ViewProduct.addActionListener(new ViewProductACTLIST());

        // Register this panel as a listener
        AppearanceSettings.getInstance().addListener(this);

        // Set initial appearance
        updateAppearance(AppearanceSettings.getInstance().getCurrentColor());
    }

    public JLabel getAppName() {
        return AppName;
    }

    private class ManageACTLIST implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ManageProducts frame = new ManageProducts(inventory);
            frame.setTitle("Manage Products");
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private class ViewProductACTLIST implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = new JFrame("View Products");
            frame.add(new ViewProductsPanel(inventory));
            frame.setSize(700, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private class AppearanceACTLIST implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame1 = new JFrame();
            frame1.add(new BuildAppearancePanel());
            frame1.setTitle("Change Appearance");
            frame1.setSize(400, 200);
            frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame1.setLocationRelativeTo(null);
            frame1.setVisible(true);
        }
    }

    public static void main(String[] args) {
        final int PANEL_WIDTH = 500;
        final int PANEL_HEIGHT = 700;
        JPanel mainpanel = new MainPagePanel();

        JFrame Frame = new JFrame();
        Frame.add(mainpanel);
        Frame.setTitle("Storage Management App");
        Frame.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setLocationRelativeTo(null);
        Frame.setVisible(true);

        // Register the frame as a listener
        AppearanceSettings.getInstance().addListener(newColor -> {
            Frame.getContentPane().setBackground(newColor);
        });

        // Set initial appearance
        Frame.getContentPane().setBackground(AppearanceSettings.getInstance().getCurrentColor());
    }

    @Override
    public void updateAppearance(Color newColor) {
        setBackground(newColor);
        buttonPanel.setBackground(newColor);
        appearanceBtn.setBackground(newColor);
        wrapperPanel.setBackground(newColor);
        middlepanel.setBackground(newColor);

        // Update text colors
        Color textColor = getContrastColor(newColor);
        AppName.setForeground(textColor);
        ManageProduct.setForeground(textColor);
        ViewProduct.setForeground(textColor);
        Appearance.setForeground(textColor);
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