package edu.uchicago.mauliafirmansyah.model;

import lombok.Data;

import java.awt.*;
import java.awt.image.ImageObserver;

// Data class representing a geometric shape
@Data
public class Shape {

    // Enumeration representing the type of shape
    public enum Type {ROUND, BOX, DOT};

    // Center point of the shape
    private Point center;

    // Size (dimension) of the shape
    private Dimension size;

    // Type of the shape (ROUND, BOX, DOT)
    private Type type;

    // Constructor for creating a shape with specified type, center, and size
    public Shape(Type type, Point center, Dimension size){
        this.type = type;
        this.center = center;
        this.size = size;
    }

    // Overloaded constructor for creating a shape with specified type, center, and size
    public Shape(Type type, Point center, int size){
        this(type, center, new Dimension(size, size));
    }

    // Overloaded constructor for creating a shape with specified type, center, width, and height
    public Shape(Type type, int centerX, int centerY, int width, int height){
        this(type, new Point(centerX, centerY), new Dimension(width, height));
    }

    // Overloaded constructor for creating a shape with specified type, center, and size
    public Shape(Type type, int centerX, int centerY, int size){
        this(type, centerX, centerY, size, size);
    }

    // Overloaded constructor for creating a dot shape at specified center
    public Shape(int centerX, int centerY){
        this(Type.DOT, centerX, centerY, 0);
    }

    // Getter method for the x-coordinate of the top-left corner of the bounding box
    public int getX0() {
        return this.center.x - this.size.width / 2;
    }

    // Getter method for the y-coordinate of the top-left corner of the bounding box
    public int getY0() {
        return this.center.y - this.size.height / 2;
    }

    // Getter method for the x-coordinate of the bottom-right corner of the bounding box
    public int getX1() {
        return this.center.x + this.size.width / 2;
    }

    // Getter method for the y-coordinate of the bottom-right corner of the bounding box
    public int getY1() {
        return this.center.y + this.size.height / 2;
    }

    // Getter method for the width of the shape
    public int getWidth() {
        return this.size.width;
    }

    // Getter method for the height of the shape
    public int getHeight() {
        return this.size.height;
    }

    // Checks whether this shape is colliding with another shape
    public boolean isColliding(Shape other) {
        // Case 1: ROUND vs. ROUND or ROUND vs. DOT or DOT vs. ROUND
        if ((this.type == Type.ROUND && other.type == Type.ROUND) ||
                (this.type == Type.ROUND && other.type == Type.DOT) ||
                (this.type == Type.DOT && other.type == Type.ROUND)) {
            // Calculate the distance between the centers of the two shapes
            double distance = Math.sqrt(
                    Math.pow(this.center.x - other.center.x, 2) +
                            Math.pow(this.center.y - other.center.y, 2)
            );
            // Check if the distance is less than the sum of their radii or half of the sum of their widths and heights
            return distance < (
                    this.size.width + this.size.height +
                            other.size.width + other.size.height
            ) / 2.0;
        }
        // Case 2: DOT vs. DOT
        else if (this.type == Type.DOT && other.type == Type.DOT) {
            // Check if the centers of the two dots are equal
            return this.center.equals(other.center);
        }
        // Case 3: BOX vs. BOX
        else if (this.type == Type.BOX && other.type == Type.BOX) {
            // Determine the overlapping region by finding horizontal and vertical distances
            int distanceX = Math.min(this.getX1(), other.getX1()) - Math.max(this.getX0(), other.getX0());
            int distanceY = Math.min(this.getY1(), other.getY1()) - Math.max(this.getY0(), other.getY0());
            // Check if both horizontal and vertical distances are positive (indicating overlap)
            return distanceX > 0 && distanceY > 0;
        }
        // Case 4: BOX vs. ROUND
        else if (this.type == Type.BOX && other.type == Type.ROUND) {
            // Find the nearest point on the box to the center of the round shape
            int nearestX = Math.max(this.getX0(), Math.min(other.center.x, this.getX1()));
            int nearestY = Math.max(this.getY0(), Math.min(other.center.y, this.getY0()));
            // Calculate the distance between the nearest point and the center of the round shape
            int distanceX = nearestX - other.center.x;
            int distanceY = nearestY - other.center.y;
            // Check if the distance is less than or equal to half of the sum of the round shape's width and height
            return Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2)) <= (other.size.width + other.size.height) / 2.0;
        }
        // Case 5: ROUND vs. BOX
        else if (this.type == Type.ROUND && other.type == Type.BOX) {
            // Call the isColliding method with the parameters swapped (delegates to the BOX vs. ROUND case)
            return other.isColliding(this);
        }
        // Case 6: BOX vs. DOT
        else if (this.type == Type.BOX && other.type == Type.DOT) {
            // Check if the center of the dot is within the bounding box of the box
            return other.center.x > this.getX0() &&
                    other.center.x < this.getX1() &&
                    other.center.y > this.getY0() &&
                    other.center.y < this.getY1();
        }
        // Case 7: DOT vs. BOX
        else if (this.type == Type.DOT && other.type == Type.BOX) {
            // Call the isColliding method with the parameters swapped (delegates to the BOX vs. DOT case)
            return other.isColliding(this);
        }
        // Default: No collision
        return false;
    }

    // Draws the shape with a specified color and dot size.
    public void draw(Graphics g, Color color, int dotSize) {
        // Set the color of the graphics context
        g.setColor(color);
        // Switch based on the type of the shape
        switch (getType()) {
            // Case: ROUND
            case ROUND:
                // Draw a filled oval representing the round shape
                g.fillOval(getX0(), getY0(), getWidth(), getHeight());
                break;
            // Case: BOX
            case BOX:
                // Draw a filled rectangle representing the box shape
                g.fillRect(getX0(), getY0(), getWidth(), getHeight());
            // Case: DOT
            case DOT:
                // Draw a filled oval representing the dot shape centered at the shape's center
                g.fillOval(getCenter().x - dotSize / 2,
                        getCenter().y - dotSize / 2,
                        dotSize, dotSize
                );
                // Default: No action (should include a break statement after each case)
            default:
                break;
        }
    }

    // Draws the shape with a specified color and a default dot size of 10
    public void draw(Graphics g, Color color) {
        // Call the draw method with the default dot size (10)
        draw(g, color, 10);
    }

    // Draws an image within the bounding box of the shape.
    public void drawImage(Graphics g, Image image, ImageObserver observer) {
        // Draw the image at the location and size of the bounding box of the shape
        g.drawImage(image, getX0(), getY0(), getWidth(), getHeight(), observer);
    }


}
