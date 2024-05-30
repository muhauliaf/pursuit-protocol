package edu.uchicago.mauliafirmansyah.view;

import edu.uchicago.mauliafirmansyah.controller.Sound;
import edu.uchicago.mauliafirmansyah.model.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

// Abstract class representing a panel with help information
public abstract class HelpPanel extends WeightedPanel implements ActionListener {
    // Background image for the help panel
    private BufferedImage bgImage;
    // Name of the back button
    private String backButtonName;

    // Constructor for the help panel
    public HelpPanel(String backButtonName_, String bgImagePath){
        // Initialize the weighted panel with specific layout
        super(16,12,6,10,4,1);
        // Set the name of the back button
        backButtonName = backButtonName_;
        // Set the layout of the internal panel
        getPanel().setLayout(new GridLayout(0,1));
        // Create and add a custom back button to the panel
        CustomButton backButton = new CustomButton(backButtonName);
        getPanel().add(backButton);
        // Add action listener for the back button
        backButton.addActionListener(this);
        // Load background image if provided
        if (bgImagePath != null) bgImage = ImageUtils.loadImage(bgImagePath);
    }

    // Method to paint the component with background image if available
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }

    // ActionListener implementation for handling button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        // Play button click sound
        Sound.playSound(Sound.SFX_BUTTON);
        // Get the text of the clicked button
        String buttonText = ((JButton)e.getSource()).getText();
        // If the button is the back button, trigger the back button action
        if (buttonText.equals(backButtonName)) {
            backButtonPressed();
        }
    }

    // Abstract method to be implemented by subclasses for back button action
    public abstract void backButtonPressed();
}
