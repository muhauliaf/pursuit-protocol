package edu.uchicago.mauliafirmansyah.model;

import edu.uchicago.mauliafirmansyah.controller.KeyInput;
import lombok.Data;

import java.awt.image.BufferedImage;

import static edu.uchicago.mauliafirmansyah.view.GameFrame.GAME_DIM;

@Data
public class PlayVehicle extends GameObject {

    // Constants defining vehicle properties
    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;
    public static final int SPEED_Y = 20;
    public static final int MIN_SPEED_X = 10;
    public static final int MAX_SPEED_X = 30;
    public static final double SCALE_SPEED_ADD_X = 1;
    public static final double SCALE_SPEED_SUB_X = 2;
    public static final int MIN_X = 100;
    public static final int MAX_X = 400;

    // Attributes specific to PlayVehicle
    private double speedX;
    private int healthPoint, healthPointMax;
    private boolean broken;
    private int minY, maxY;
    public static final BufferedImage IMAGE = ImageUtils.loadImage("/imgs/cars/police_car.png");

    // Constructor with parameters for minimum Y, maximum Y, and health points
    public PlayVehicle(int minY_, int maxY_, int healthPoint_){
        super(Team.FRIEND, new Shape(Shape.Type.BOX,0,0, WIDTH, HEIGHT));
        minY = minY_;
        maxY = maxY_;
        updateSpeedX(MIN_SPEED_X);
        getCenter().y = GAME_DIM.height/2;
        healthPointMax = healthPoint_;
        healthPoint = healthPointMax;
        broken = false;
        image = IMAGE;
    }

    // Method to update the speed along the X-axis
    public void updateSpeedX(double sx) {
        speedX = sx;
        this.getCenter().x = MIN_X + (int)
                ((MAX_X-MIN_X)*(speedX-MIN_SPEED_X)/(MAX_SPEED_X-MIN_SPEED_X))
        ;
    }

    // Override the move method to restrict the vehicle within the specified Y range
    @Override
    public void move() {
        super.move();
        if (getCenter().y < minY) getCenter().y = minY;
        if (getCenter().y > maxY) getCenter().y = maxY;
    }

    // Method to update the vehicle's input based on key presses
    public void updateInput(KeyInput keyInput) {
        if (keyInput.isKeyPress(KeyInput.UP) ^ keyInput.isKeyPress(KeyInput.DOWN)){
            this.getDelta().y = keyInput.isKeyPress(KeyInput.DOWN) ? SPEED_Y : -SPEED_Y;
        } else {
            this.getDelta().y = 0;
        }
        if (keyInput.isKeyPress(KeyInput.LEFT) && !keyInput.isKeyPress(KeyInput.RIGHT)){
            updateSpeedX(Math.max(speedX - SCALE_SPEED_SUB_X, MIN_SPEED_X));
        }
        if (keyInput.isKeyPress(KeyInput.RIGHT) && !keyInput.isKeyPress(KeyInput.LEFT)){
            updateSpeedX(Math.min(speedX + SCALE_SPEED_ADD_X, MAX_SPEED_X));
        }
    }

    // Method to create and return a Bullet object when shooting
    public Bullet shoot() {
        System.out.println("SHOOT!");
        Bullet bullet = new Bullet(team);
        bullet.getCenter().x = this.shape.getX1();
        bullet.getCenter().y = this.getCenter().y;
        bullet.getDelta().x = 30;
        return bullet;
    }

    // Method to inflict damage to the vehicle and handle damage-related actions
    public void damage(int damagePoint) {
        System.out.println("OUCH!");
        healthPoint -= damagePoint;
        if (healthPoint <= 0){
            System.out.println("NOOOO!!");
            setBroken(true);
        }
    }
}
