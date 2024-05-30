package edu.uchicago.mauliafirmansyah.model;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

@Data
public abstract class GameObject implements Movable, Teamable {

    // Shape representing the object's position and dimensions
    protected Shape shape;

    // Team to which the object belongs (FRIEND, CIVILIAN, ENEMY)
    protected Team team;

    // Delta represents the change in position for movement
    protected Point delta;

    // Flag indicating whether the object is expired or not
    protected boolean expired;

    // Image to be used for rendering the object
    protected BufferedImage image;

    // Color to be used for rendering the object if no image is provided
    protected Color color;

    // Constructor with parameters for team and shape
    public GameObject(Team team_, Shape shape_) {
        team = team_;
        shape = shape_;
        delta = new Point();
        expired = false;
        color = Color.BLUE;
    }

    // Default constructor with FRIEND team and a default shape
    public GameObject() {
        this(Team.FRIEND, new Shape(0, 0));
    }

    // Get the center of the object's shape
    public Point getCenter() {
        return shape.getCenter();
    }

    // Move method to update the position based on the delta values
    public void move() {
        getCenter().x += delta.x;
        getCenter().y += delta.y;
    }

    // Draw method to render the object using either an image or a shape with a color
    public void draw(Graphics g) {
        if (image == null) {
            shape.draw(g, color, 10);
        } else {
            shape.drawImage(g, image, null);
        }
    }
}
