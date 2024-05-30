package edu.uchicago.mauliafirmansyah.model;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

@Data
public class UIObject {
    private boolean visible;        // Represents the visibility status of the UIObject
    private Shape shape;            // Represents the shape of the UIObject
    protected BufferedImage image; // Image associated with the UIObject

    // Constructor for UIObject
    public UIObject(Shape shape_, BufferedImage image_) {
        shape = shape_;
        image = image_;
        visible = false;  // Initialize visibility to false
    }

    // Draw method for rendering the UIObject on the graphics context
    public void draw(Graphics g) {
        if (visible) {
            // Draw the UIObject only if it is marked as visible
            if (image == null) {
                // If no image is provided, draw the shape with a default color and dot size
                shape.draw(g, Color.LIGHT_GRAY, 10);
            } else {
                // If an image is provided, draw the image using the shape's coordinates
                shape.drawImage(g, image, null);
            }
        }
    }
}
