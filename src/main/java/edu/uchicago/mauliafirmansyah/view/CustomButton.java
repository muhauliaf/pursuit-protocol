package edu.uchicago.mauliafirmansyah.view;

import javax.swing.*;
import java.awt.*;

// CustomButton class extends JButton to create a customized button
public class CustomButton extends JButton {

    // Font size constant for the button text
    public static final int FONT_SIZE = 20;

    // Constructor for CustomButton, takes a text as a parameter
    public CustomButton(String text) {
        // Call the constructor of the superclass (JButton) with the provided text
        super(text);

        // Set the font for the button text to "Impact", bold, with the specified font size
        setFont(new Font("Impact", Font.BOLD, FONT_SIZE));

        // Set margin to provide negative space around the text for better visual appearance
        setMargin(new Insets(-FONT_SIZE, -FONT_SIZE, -FONT_SIZE, -FONT_SIZE));

        // Set the text color to white
        setForeground(Color.WHITE);

        // Set the background color to dark gray
        setBackground(Color.DARK_GRAY);

        // Make the content area transparent
        setContentAreaFilled(false);

        // Set the button to be opaque (non-transparent)
        setOpaque(true);
    }
}
