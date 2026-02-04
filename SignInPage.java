package com.mycompany.java2project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SignInPage extends JPanel implements AppearanceSettings.AppearanceListener {

    private JLabel appName;
    private JLabel signIn;
    private JLabel username;
    private JLabel password;
    public JTextField usernameField;
    public JTextField passwordField;
    private JPanel signinpanel;
    private JPanel usernamepanel;
    private JPanel signinlabel;
    private JButton signinbtn;
    private JPanel signbtnpanel;
    private JButton CreateAccount;

    public SignInPage() {
        // Initialize components
        appName = new JLabel("Storage Management App", SwingConstants.CENTER);
        signIn = new JLabel("Sign In: ");
        username = new JLabel("Username:");
        password = new JLabel("Password:");
        usernameField = new JTextField(10);
        passwordField = new JTextField(10);
        signinbtn = new JButton("Sign in");
        CreateAccount = new JButton("Register");

        // Create panels
        signinpanel = new JPanel();
        signinpanel.setLayout(new BorderLayout());
        usernamepanel = new JPanel();
        usernamepanel.setLayout(new FlowLayout());
        usernamepanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        signinlabel = new JPanel();
        signinlabel.setLayout(new FlowLayout());
        signbtnpanel = new JPanel();
        signbtnpanel.setLayout(new FlowLayout());

        // Add components to panels
        signinlabel.add(signIn);
        usernamepanel.add(username);
        usernamepanel.add(usernameField);
        usernamepanel.add(password);
        usernamepanel.add(passwordField);
        signbtnpanel.add(signinbtn);
        signbtnpanel.add(CreateAccount);

        // Button Action Listeners
        signinbtn.addActionListener(new SignInAction());
        CreateAccount.addActionListener(new RegisterAction());

        // Add panels to the main sign-in panel
        signinpanel.add(appName, BorderLayout.NORTH);
        signinpanel.add(signinlabel, BorderLayout.WEST);
        signinpanel.add(usernamepanel, BorderLayout.CENTER);
        signinpanel.add(signbtnpanel, BorderLayout.SOUTH);
        add(signinpanel);

        // Register this panel as a listener
        AppearanceSettings.getInstance().addListener(this);

        // Set initial appearance
        updateAppearance(AppearanceSettings.getInstance().getCurrentColor());
    }

    // SignInAction as a private inner class
    private class SignInAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Get entered username and password
            String enteredUsername = usernameField.getText().trim();
            String enteredPassword = passwordField.getText().trim();

            // Check if fields are empty
            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                JOptionPane.showMessageDialog(signinpanel, "Username or password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate credentials
            boolean isAuthenticated = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("user_credentials.txt"))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(",");

                    if (credentials.length == 2) {
                        String storedUsername = credentials[0].trim();
                        String storedPassword = credentials[1].trim();

                        if (storedUsername.equals(enteredUsername) && storedPassword.equals(enteredPassword)) {
                            isAuthenticated = true;
                            break;
                        }
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(signinpanel, "Error reading credentials file.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Display result to user
            if (isAuthenticated) {
                JOptionPane.showMessageDialog(signinpanel, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Proceed to main page
                JFrame mainFrame = new JFrame("Main Page");
                mainFrame.add(new MainPagePanel());
                mainFrame.setSize(500, 600);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);

                // Close the sign-in window
                SwingUtilities.getWindowAncestor(SignInPage.this).dispose();
            } else {
                JOptionPane.showMessageDialog(signinpanel, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // RegisterAction as a separate private inner class
    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a new JFrame for RegisterPage
            JFrame RegisterFrames = new JFrame("Register Page");
            RegisterFrames.add(new RegisterPage());
            RegisterFrames.setSize(400, 125);
            RegisterFrames.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            RegisterFrames.setLocationRelativeTo(null);
            RegisterFrames.setVisible(true);
        }
    }

    public static void main(String[] args) {
        final int PANEL_WIDTH = 500;
        final int PANEL_HEIGHT = 150;
        JPanel signinpanel1 = new SignInPage();
        JFrame flame = new JFrame();

        // Register the frame as a listener
        AppearanceSettings.getInstance().addListener(newColor -> {
            flame.getContentPane().setBackground(newColor);
        });

        // Set initial appearance
        flame.getContentPane().setBackground(AppearanceSettings.getInstance().getCurrentColor());

        flame.add(signinpanel1);
        flame.setTitle("Sign In Page");
        flame.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        flame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        flame.setLocationRelativeTo(null);
        flame.setVisible(true);
    }

    @Override
    public void updateAppearance(Color newColor) {
        // Update background colors
        setBackground(newColor);
        signinpanel.setBackground(newColor);
        usernamepanel.setBackground(newColor);
        signinlabel.setBackground(newColor);
        signbtnpanel.setBackground(newColor);

        // Update text colors
        Color textColor = getContrastColor(newColor);
        appName.setForeground(textColor);
        signIn.setForeground(textColor);
        username.setForeground(textColor);
        password.setForeground(textColor);
        signinbtn.setForeground(textColor);
        CreateAccount.setForeground(textColor);
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
