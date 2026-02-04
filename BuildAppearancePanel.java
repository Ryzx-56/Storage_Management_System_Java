package com.mycompany.java2project2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BuildAppearancePanel extends JPanel {
    // Create the Appearance Buttons and Labels
    private JRadioButton blackcolor;
    private JRadioButton Greycolor;
    private JRadioButton Redcolor;
    private JRadioButton Whitecolor;
    private JRadioButton Cyancolor;
    private JLabel changeAppearance;
    private JButton exit;
    private JPanel AppearancebtnPanel;
    private ButtonGroup buttongroup;
    private JPanel colorbtns;

    public BuildAppearancePanel() {
        AppearancebtnPanel = new JPanel();
        AppearancebtnPanel.setLayout(new BorderLayout());

        blackcolor = new JRadioButton("Black");
        Greycolor = new JRadioButton("Grey", true);
        Redcolor = new JRadioButton("Red");
        Whitecolor = new JRadioButton("White");
        Cyancolor = new JRadioButton("Cyan");
        changeAppearance = new JLabel("Change Appearance Color:", SwingConstants.CENTER);
        exit = new JButton("EXIT");

        // Make exit button smaller in the panel
        JPanel size = new JPanel();
        size.setLayout(new FlowLayout(FlowLayout.RIGHT));
        size.add(exit);

        // Action Listener for exit button
        exit.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow instanceof JFrame) {
                parentWindow.dispose();
            }
        });

        colorbtns = new JPanel();
        colorbtns.setLayout(new GridLayout(5, 1));
        buttongroup = new ButtonGroup();
        buttongroup.add(blackcolor);
        buttongroup.add(Greycolor);
        buttongroup.add(Redcolor);
        buttongroup.add(Whitecolor);
        buttongroup.add(Cyancolor);

        blackcolor.addActionListener(new ColorActionList(Color.BLACK));
        Greycolor.addActionListener(new ColorActionList(Color.GRAY));
        Redcolor.addActionListener(new ColorActionList(Color.RED));
        Whitecolor.addActionListener(new ColorActionList(Color.WHITE));
        Cyancolor.addActionListener(new ColorActionList(Color.CYAN));

        colorbtns.add(blackcolor);
        colorbtns.add(Greycolor);
        colorbtns.add(Redcolor);
        colorbtns.add(Whitecolor);
        colorbtns.add(Cyancolor);

        AppearancebtnPanel.add(changeAppearance, BorderLayout.NORTH);
        AppearancebtnPanel.add(colorbtns, BorderLayout.WEST);
        AppearancebtnPanel.add(size, BorderLayout.SOUTH);
        this.setLayout(new BorderLayout());
        this.add(AppearancebtnPanel, BorderLayout.CENTER);

        // Register this panel as a listener
        AppearanceSettings.getInstance().addListener(newColor -> {
            updateAppearance(newColor);
        });

        // Set initial appearance
        updateAppearance(AppearanceSettings.getInstance().getCurrentColor());
    }

    // Color change for buttons
    private class ColorActionList implements ActionListener {
        private final Color color;

        public ColorActionList(Color color) {
            this.color = color;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Set the new color in AppearanceSettings
            AppearanceSettings.getInstance().setCurrentColor(color);
        }
    }

    private void updateAppearance(Color newColor) {
        setBackground(newColor);
        AppearancebtnPanel.setBackground(newColor);
        colorbtns.setBackground(newColor);

        // Update text colors
        Color textColor = getContrastColor(newColor);
        changeAppearance.setForeground(textColor);
        blackcolor.setForeground(textColor);
        Greycolor.setForeground(textColor);
        Redcolor.setForeground(textColor);
        Whitecolor.setForeground(textColor);
        Cyancolor.setForeground(textColor);
        exit.setForeground(textColor);
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