package edu.uchicago.mauliafirmansyah.model;

import java.awt.*;

// Bullet class representing projectiles in the game
public class Bullet extends GameObject {

    // Constructor to initialize Bullet attributes based on the team
    public Bullet(Team team_) {
        super(team_, new Shape(0,0));
        // Set color based on the team (YELLOW for FRIEND, RED for ENEMY)
        color = team == Team.FRIEND ? Color.YELLOW : Color.RED;
    }

    // Method to check collision with another GameObject
    public boolean checkCollision(GameObject gameObject) {
        // Check if the Bullet collides with the shape of the given GameObject
        if(gameObject.getShape().isColliding(shape)) {
            // If collision occurs, mark the Bullet as expired and return true
            setExpired(true);
            return true;
        }
        // Return false if no collision occurs
        return false;
    }
}
