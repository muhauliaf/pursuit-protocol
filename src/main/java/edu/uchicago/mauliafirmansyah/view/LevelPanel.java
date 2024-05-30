package edu.uchicago.mauliafirmansyah.view;

import edu.uchicago.mauliafirmansyah.controller.Sound;
import edu.uchicago.mauliafirmansyah.model.ImageUtils;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

// Abstract class representing a panel for different levels
@Data
public abstract class LevelPanel extends WeightedPanel implements ActionListener {
    // Background image for the level panel
    private BufferedImage bgImage;
    // Name of the level button
    private String levelButtonName;
    // Name of the back button
    private String backButtonName;

    // Constructor for the level panel
    public LevelPanel(int levelNumber, String levelButtonName_, String backButtonName_, String bgImagePath){
        // Initialize the weighted panel with specific layout
        super(16,12,3,9,10,2);
        // Set the name of the level button and back button
        levelButtonName = levelButtonName_;
        backButtonName = backButtonName_;
        // Set the layout of the internal panel
        getPanel().setLayout(new GridLayout(0,5));
        // Create level buttons based on the given level number
        for (int i = 1; i <= levelNumber; i++) {
            CustomButton button = new CustomButton(levelButtonName+" "+i);
            getPanel().add(button);
            button.addActionListener(this);
        }
        // Create and add a custom back button to the panel
        CustomButton backButton = new CustomButton(backButtonName);
        getPanel().add(backButton);
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
        // If the button is a level button, trigger the level button action
        if (buttonText.startsWith(levelButtonName)) {
            levelButtonPressed(Integer.parseInt(buttonText.replace(levelButtonName+" ","")));
        }
        // If the button is the back button, trigger the back button action
        else if (buttonText.equals(backButtonName)) {
            backButtonPressed();
        }
    }

    // Abstract method to be implemented by subclasses for level button action
    public abstract void levelButtonPressed(int level);

    // Abstract method to be implemented by subclasses for back button action
    public abstract void backButtonPressed();
}
