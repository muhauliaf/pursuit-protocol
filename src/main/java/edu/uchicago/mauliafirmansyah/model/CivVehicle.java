package edu.uchicago.mauliafirmansyah.model;

import lombok.Data;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

@Data
public class CivVehicle extends GameObject {

    // Constants defining vehicle properties
    public static final int MAX_SPEED = -2;
    public static final int MIN_SPEED = -5;

    public static final int BROKEN_Y_SPEED = 10;

    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;
    public final static Random R = new Random();

    // Attributes specific to CivVehicle
    private int defaultSpeed;
    private boolean broken;
    private int type;

    // 2D array to store civilian car images and broken images
    public static final BufferedImage[][] IMAGES = loadImages();

    // Method to load images for civilian cars
    private static BufferedImage[][] loadImages() {
        BufferedImage[][] images = new BufferedImage[6][2];
        for (int i = 0; i < images.length; i++) {
            images[i][0] = ImageUtils.loadImage("/imgs/cars/civilian_car_0"+(i+1)+".png");
            images[i][1] = ImageUtils.loadImage("/imgs/cars/civilian_car_0"+(i+1)+"_broken.png");
        }
        return images;
    }

    // Constructor to initialize CivVehicle attributes
    public CivVehicle() {
        super(Team.CIVILIAN, new Shape(Shape.Type.BOX,0,0,WIDTH,HEIGHT));
        defaultSpeed = MIN_SPEED + R.nextInt(MAX_SPEED - MIN_SPEED);
        color = Color.GREEN;
        type = R.nextInt(IMAGES.length);
        image = IMAGES[type][0];
    }

    // Method to move the civilian vehicle (with or without reference to PlayVehicle)
    public void move(PlayVehicle playVehicle) {
        this.getCenter().x += defaultSpeed - (playVehicle == null ? 0 : (int) (playVehicle.getSpeedX()));
        if(this.getShape().getX1() < 0){
            this.getDelta().x = 0;
            this.setExpired(true);
        }
        getCenter().y += delta.y;
    }

    // Overridden move method without reference to PlayVehicle
    @Override
    public void move() {
        move(null);
    }

    // Method to check for collisions with other GameObjects
    public boolean checkCollision(GameObject gameObject) {
        if(!broken && gameObject.getShape().isColliding(shape)) {
            setBroken(true);
            if (getCenter().y > gameObject.getCenter().y) {
                this.delta.y = BROKEN_Y_SPEED;
            } else {
                this.delta.y = BROKEN_Y_SPEED * -1;
            }
            image = IMAGES[type][1];
            color = Color.GRAY;
            return true;
        }
        return false;
    }
}
