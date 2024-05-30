package edu.uchicago.mauliafirmansyah.view;

import edu.uchicago.mauliafirmansyah.controller.Sound;
import edu.uchicago.mauliafirmansyah.model.ImageUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

// Abstract class representing a menu panel
public abstract class MenuPanel extends WeightedPanel implements ActionListener {

    // Background image for the menu panel
    private BufferedImage bgImage;

    // Constructor for the menu panel
    public MenuPanel(String[] buttonTexts, String bgImagePath){
        // Initialize the weighted panel with specific layout
        super(16,12,6,8,4,3);
        // Set the layout of the internal panel
        this.getPanel().setLayout(new GridLayout(0,1));
        // Create custom buttons based on the given buttonTexts
        for (String buttonText: buttonTexts) {
            CustomButton button = new CustomButton(buttonText);
            this.getPanel().add(button);
            button.addActionListener(this);
        }
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
        // Get the text of the clicked button and trigger the buttonPressed method
        this.buttonPressed(((JButton)e.getSource()).getText());
    }

    // Abstract method to be implemented by subclasses for button press action
    public abstract void buttonPressed(String text);
}
