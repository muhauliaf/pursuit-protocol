package edu.uchicago.mauliafirmansyah.view;

import javax.swing.*;
import java.awt.*;

// Represents an empty component with no visible content
public class EmptyComponent extends JComponent {

    // Constructor to initialize the empty component
    public EmptyComponent() {
        // Make the component transparent
        setOpaque(false);
    }

    // Paint component method (overridden from JComponent)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // No additional painting for an empty component
    }
}