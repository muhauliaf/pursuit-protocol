package edu.uchicago.mauliafirmansyah.model;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static edu.uchicago.mauliafirmansyah.view.GameFrame.GAME_DIM;

@Data
public class BadVehicle extends GameObject implements Randomable {

    // Constants defining vehicle properties
    public static final int X_SPAWN_SPEED = 5;
    public static final int BROKEN_Y_SPEED = 10;

    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;

    private boolean active;
    private int spawnDelaySeconds;
    private int shootDelayMax;
    private int shootDelay;
    private int minY, maxY, startY;
    private int positionX;
    private int speedY;
    private int healthPoint, healthPointMax;
    private int enemyType;
    private int bulletSpeed;
    private boolean bulletHoming;
    private boolean broken;

    // 2D array to store bad guy car images and broken images
    public static final BufferedImage[][] IMAGES = loadImages();

    // Method to load images for bad guy cars
    private static BufferedImage[][] loadImages() {
        BufferedImage[][] images = new BufferedImage[5][2];
        for (int i = 0; i < images.length; i++) {
            images[i][0] = ImageUtils.loadImage("/imgs/cars/badguy_car_0" + (i + 1) + ".png");
            images[i][1] = ImageUtils.loadImage("/imgs/cars/badguy_car_0" + (i + 1) + "_broken.png");
        }
        return images;
    }

    // Constructor to initialize BadVehicle attributes
    public BadVehicle(int spawnDelaySeconds_, int minY_, int maxY_, int startY_, int positionX_, int healthPoint_, int enemyType_) {
        super(Team.ENEMY, new Shape(Shape.Type.BOX, 0, 0, WIDTH, HEIGHT));
        active = false;
        broken = false;
        spawnDelaySeconds = spawnDelaySeconds_;
        minY = minY_;
        maxY = maxY_;
        startY = startY_;
        positionX = positionX_;
        healthPointMax = healthPoint_;
        healthPoint = healthPointMax;
        enemyType = enemyType_;
        switch (enemyType) {
            case 1:
                speedY = 0;
                bulletHoming = false;
                bulletSpeed = 20;
                shootDelayMax = 40;
                break;
            case 2:
                speedY = 10 + R.nextInt(3);
                bulletHoming = false;
                bulletSpeed = 20;
                shootDelayMax = 40;
                break;
            case 3:
                speedY = 10 + R.nextInt(3);
                bulletHoming = false;
                bulletSpeed = 20;
                shootDelayMax = 25;
                break;
            case 4:
                speedY = 10 + R.nextInt(3);
                bulletHoming = true;
                bulletSpeed = 20;
                shootDelayMax = 40;
                break;
            case 5:
                speedY = 10 + R.nextInt(3);
                bulletHoming = true;
                bulletSpeed = 20;
                shootDelayMax = 25;
                break;
            default:
        }
        shootDelay = shootDelayMax;
        delta.y = speedY;
        color = Color.RED;
        image = IMAGES[enemyType - 1][0];
    }

    // Method to spawn BadVehicle
    public void spawn() {
        active = true;
        getCenter().setLocation(
                GAME_DIM.width + shape.getWidth() / 2,
                startY
        );
    }

    // Overridden move method for BadVehicle
    @Override
    public void move() {
        super.move();
        if (active) {
            if (broken) {
                if (getShape().getY1() < 0 || getShape().getY0() > GAME_DIM.height) {
                    getDelta().y = 0;
                    setExpired(true);
                }
            } else {
                if (getCenter().x > positionX) getDelta().x = -X_SPAWN_SPEED;
                else getDelta().x = 0;
                if (getCenter().y <= minY) getDelta().y = speedY;
                if (getCenter().y >= maxY) getDelta().y = -speedY;
            }
        }
    }

    // Method for BadVehicle to shoot bullets
    public Bullet shoot(PlayVehicle playVehicle) {
        if (active && !broken) {
            if (shootDelay <= 0) {
                shootDelay = shootDelayMax + R.nextInt(5);
                Bullet bullet = new Bullet(team);
                bullet.getCenter().x = this.shape.getX0();
                bullet.getCenter().y = this.getCenter().y;
                if (bulletHoming) {
                    int deltaX = playVehicle.getCenter().x - this.getCenter().x;
                    int deltaY = playVehicle.getCenter().y - this.getCenter().y;
                    int distance = (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                    bullet.getDelta().x = bulletSpeed * deltaX / distance;
                    bullet.getDelta().y = bulletSpeed * deltaY / distance;
                } else {
                    bullet.getDelta().x = -bulletSpeed;
                }
                return bullet;
            } else {
                shootDelay--;
            }
        }
        return null;
    }

    // Method to handle damage to BadVehicle
    public void damage(int damagePoint) {
        System.out.println("HIT!");
        healthPoint -= damagePoint;
        if (healthPoint <= 0) {
            System.out.println("BREAK!!");
            setBroken(true);
        }
    }

    // Method to set BadVehicle as broken
    public void setBroken(boolean broken_) {
        if (active && !broken) {
            broken = broken_;
            delta.x = BROKEN_Y_SPEED;
            delta.y = BROKEN_Y_SPEED;
            image = IMAGES[enemyType - 1][1];
        }
    }

    // Overridden draw method for BadVehicle
    @Override
    public void draw(Graphics g) {
        if (active) super.draw(g);
    }
}
