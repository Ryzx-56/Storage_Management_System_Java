package com.mycompany.java2project2;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class RegisterPage extends JPanel implements AppearanceSettings.AppearanceListener {
    private JLabel Register_Username;
    private JLabel Register_Password;
    public JTextField Register_UsernameField;
    public JTextField Register_PasswordField;
    private JButton RegisterBtn;
    private JPanel Registerpanel;
    private JPanel flowRegister;
    private JPanel btnfix;

    public RegisterPage() {
        // Creating the register page
        Registerpanel = new JPanel();
        Registerpanel.setLayout(new BorderLayout());

        Register_Username = new JLabel("Username:");
        Register_UsernameField = new JTextField(10);
        Register_Password = new JLabel("Password:");
        Register_PasswordField = new JTextField(10);
        RegisterBtn = new JButton("Register");

        flowRegister = new JPanel();
        flowRegister.setLayout(new FlowLayout());

        flowRegister.add(Register_Username);
        flowRegister.add(Register_UsernameField);
        flowRegister.add(Register_Password);
        flowRegister.add(Register_PasswordField);

        btnfix = new JPanel();
        btnfix.setLayout(new FlowLayout());
        btnfix.add(RegisterBtn);
        Registerpanel.add(flowRegister, BorderLayout.CENTER);
        Registerpanel.add(btnfix, BorderLayout.SOUTH);
        add(Registerpanel);

        RegisterBtn.addActionListener(new RegisterAction());

        // Register this panel as a listener
        AppearanceSettings.getInstance().addListener(this);

        // Set initial appearance
        updateAppearance(AppearanceSettings.getInstance().getCurrentColor());
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Retrieve entered username and password
            String username = Register_UsernameField.getText().trim();
            String password = Register_PasswordField.getText().trim();

            // Validate input fields
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(Registerpanel, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Save username and password to a file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_credentials.txt", true))) {
                writer.write(username + "," + password);
                writer.newLine();
                JOptionPane.showMessageDialog(Registerpanel, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear input fields after successful registration
                Register_UsernameField.setText("");
                Register_PasswordField.setText("");

                // Close the registration window
                SwingUtilities.getWindowAncestor(RegisterPage.this).dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(Registerpanel, "An error occurred while saving the credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        final int PANEL_WIDTH = 400;
        final int PANEL_HEIGHT = 125;
        JPanel Registerpanel1 = new RegisterPage();
        JFrame fxame = new JFrame();

        // Register the frame as a listener
        AppearanceSettings.getInstance().addListener(newColor -> {
            fxame.getContentPane().setBackground(newColor);
        });

        // Set initial appearance
        fxame.getContentPane().setBackground(AppearanceSettings.getInstance().getCurrentColor());

        fxame.add(Registerpanel1);
        fxame.setTitle("Register New Account");
        fxame.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        fxame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fxame.setLocationRelativeTo(null);
        fxame.setVisible(true);
    }

    @Override
    public void updateAppearance(Color newColor) {
        // Update background colors
        setBackground(newColor);
        Registerpanel.setBackground(newColor);
        flowRegister.setBackground(newColor);
        btnfix.setBackground(newColor);

        // Update text colors
        Color textColor = getContrastColor(newColor);
        Register_Username.setForeground(textColor);
        Register_Password.setForeground(textColor);
        RegisterBtn.setForeground(textColor);
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