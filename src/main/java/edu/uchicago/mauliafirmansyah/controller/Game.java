package edu.uchicago.mauliafirmansyah.controller;

import edu.uchicago.mauliafirmansyah.model.Shape;
import edu.uchicago.mauliafirmansyah.model.*;
import edu.uchicago.mauliafirmansyah.view.GameFrame;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

@Data
public class Game implements Runnable {
    // Constants for game settings
    public final static int ANIMATION_DELAY = 40;
    public final static int FPS = 1000 / ANIMATION_DELAY;
    public final static Random R = new Random();
    public final static double MIN_CIV_SPAWN_DELAY = 1.0;
    public final static double MAX_CIV_SPAWN_DELAY = 2.0;
    public final static int ROAD_EXTENSION_Y = 40;
    public final static int COOLDOWN_SECONDS = 3;

    // Game objects and state
    private GameObjectFactory objectFactory;
    private PlayVehicle playVehicle;
    private List<CivVehicle> civVehicles;
    private List<BadVehicle> badVehicles;
    private List<Bullet> bullets;
    private JPanel gamePanel;
    private Thread animationThread;
    private KeyInput keyInput;
    private GameState gameState;
    private double civilianSpawnDelay;
    private boolean activeEnemy;
    private boolean musicPlaying;
    private boolean playerShooting;
    private int enemySpawnTime;
    private int maxEnemySpawnTime;
    private int cooldownFrames;
    private Map<String, UIObject> uiObjects;
    private BufferedImage[] roadImages;
    private int roadTranslateX;

    // Game level and boundaries
    private int level;
    private int minY, maxY;

    // Constructor initializes the game
    public Game(int level_) {
        // Initialize level-specific parameters
        level = level_;
        switch (level) {
            case 1:
            case 2:
            case 3:
                minY = 200;
                maxY = 450;
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                minY = 150;
                maxY = 550;
                break;
        }

        // Initialize game objects, UI, and other parameters
        initGameObjects();
        initGameUIs();

        // Set up the game panel
        gamePanel = new JPanel() {
            @Override
            public void update(Graphics g) {
                if (g != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    Dimension frameDim = getRootPane().getSize();
                    Image bufferImg = gamePanel.createImage(GameFrame.GAME_DIM.width, GameFrame.GAME_DIM.height);
                    Graphics bufferG = bufferImg.getGraphics();
                    bufferG.setColor(Color.RED);
                    bufferG.fillRect(0, 0, frameDim.width, frameDim.height);
                    Game.this.update();
                    Game.this.paint(bufferG);
                    AffineTransform g2dTransform = g2d.getTransform();
                    double zoomWidth = 1.0 * frameDim.width / GameFrame.GAME_DIM.width;
                    double zoomHeight = 1.0 * frameDim.height / GameFrame.GAME_DIM.height;
                    g2dTransform.scale(zoomWidth, zoomHeight);
                    g2d.setTransform(g2dTransform);
                    g2d.drawImage(bufferImg, 0, 0, this);
                }
            }
        };

        // Initialize game-related variables
        objectFactory = new GameObjectFactory();
        activeEnemy = false;
        musicPlaying = false;
        roadTranslateX = 0;
        setGameState(GameState.PLAY);

        // Set up key input and play game music
        keyInput = new KeyInput() {
            @Override
            public void keyCodePressed(int keyCode) {
                if (keyCode == SPACE) {
                    synchronized (this) {
                        playerShooting = true;
                    }
                }
            }
        };
        gamePanel.addKeyListener(keyInput);
        Sound.playMusic(Sound.MUSIC_GAME1);

        // Start the animation thread
        animationThread = new Thread(this);
        animationThread.start();
    }

    // Initialize game objects based on the level
    private void initGameObjects() {
        playVehicle = new PlayVehicle(minY, maxY, 100);
        civVehicles = new ArrayList<>();
        badVehicles = new ArrayList<>();
        bullets = new ArrayList<>();
        civilianSpawnDelay = 0;
        // Populate badVehicles based on the level
        switch (level) {
            case 1:
                badVehicles.add(new BadVehicle(30, minY, maxY, 250, 600, 20, 1));
                badVehicles.add(new BadVehicle(60, minY, maxY, 400, 600,20, 2));
                break;
            case 2:
                badVehicles.add(new BadVehicle(30, minY, maxY, 300, 600, 20, 2));
                badVehicles.add(new BadVehicle(60, minY, maxY, 300, 600, 20, 3));
                break;
            case 3:
                badVehicles.add(new BadVehicle(30, minY, maxY, 300, 600, 20, 4));
                badVehicles.add(new BadVehicle(60, minY, maxY, 300, 600, 20, 5));
                break;
            case 4:
                badVehicles.add(new BadVehicle(30, minY, maxY, 300, 600, 20, 3));
                badVehicles.add(new BadVehicle(60, minY, maxY, 200, 600, 20, 2));
                badVehicles.add(new BadVehicle(60, minY, maxY, 500, 720, 20, 3));
                break;
            case 5:
                badVehicles.add(new BadVehicle(30, minY, maxY, 300, 600, 20, 4));
                badVehicles.add(new BadVehicle(60, minY, maxY, 200, 600, 20, 4));
                badVehicles.add(new BadVehicle(60, minY, maxY, 500, 720, 20, 3));
                break;
            case 6:
                badVehicles.add(new BadVehicle(30, minY, maxY, 200, 600, 20, 4));
                badVehicles.add(new BadVehicle(30, minY, maxY, 500, 720, 20, 4));
                badVehicles.add(new BadVehicle(60, minY, maxY, 200, 530, 20, 3));
                badVehicles.add(new BadVehicle(60, minY, maxY, 350, 640, 20, 3));
                badVehicles.add(new BadVehicle(60, minY, maxY, 500, 750, 20, 4));
                break;
            case 7:
                badVehicles.add(new BadVehicle(30, minY, maxY, 200, 600, 20, 4));
                badVehicles.add(new BadVehicle(30, minY, maxY, 500, 720, 20, 5));
                badVehicles.add(new BadVehicle(60, minY, maxY, 200, 530, 20, 3));
                badVehicles.add(new BadVehicle(60, minY, maxY, 350, 640, 20, 4));
                badVehicles.add(new BadVehicle(60, minY, maxY, 500, 750, 20, 5));
                break;
            case 8:
                badVehicles.add(new BadVehicle(30, minY, maxY, 200, 530, 20, 4));
                badVehicles.add(new BadVehicle(30, minY, maxY, 350, 640, 20, 4));
                badVehicles.add(new BadVehicle(30, minY, maxY, 500, 750, 20, 4));
                badVehicles.add(new BadVehicle(60, minY, maxY, 200, 530, 20, 5));
                badVehicles.add(new BadVehicle(60, minY, maxY, 350, 640, 20, 5));
                badVehicles.add(new BadVehicle(60, minY, maxY, 500, 750, 20, 5));
                break;
            case 9:
                badVehicles.add(new BadVehicle(10, minY, maxY, 200, 750, 20, 5));
                badVehicles.add(new BadVehicle(10, minY, maxY, 350, 750, 20, 5));
                badVehicles.add(new BadVehicle(10, minY, maxY, 500, 750, 20, 5));
                badVehicles.add(new BadVehicle(10, minY, maxY, 200, 640, 20, 4));
                badVehicles.add(new BadVehicle(10, minY, maxY, 350, 640, 20, 4));
                badVehicles.add(new BadVehicle(10, minY, maxY, 500, 640, 20, 4));
                badVehicles.add(new BadVehicle(10, minY, maxY, 200, 530, 20, 3));
                badVehicles.add(new BadVehicle(10, minY, maxY, 350, 530, 20, 3));
                badVehicles.add(new BadVehicle(10, minY, maxY, 500, 530, 20, 3));
                break;
        }
        maxEnemySpawnTime = badVehicles.stream()
                .mapToInt(x->x.getSpawnDelaySeconds()*FPS)
                .max().orElse(0);
    }

    // Initialize game UI elements
    private void initGameUIs() {
        uiObjects = new HashMap<>();
        // Populate uiObjects with relevant UI elements and images
        uiObjects.put("banner_win", new UIObject(
                new Shape(Shape.Type.BOX,
                        400, 300,
                        400, 300
                        ),
                ImageUtils.loadImage("/imgs/ui/banner_win.png")
        ));
        uiObjects.put("banner_lose", new UIObject(
                new Shape(Shape.Type.BOX,
                        400, 300,
                        400, 300
                ),
                ImageUtils.loadImage("/imgs/ui/banner_lose.png")
        ));
        uiObjects.put("icon_policecar", new UIObject(
                new Shape(Shape.Type.BOX,
                        0, 0,
                        20, 20
                ),
                ImageUtils.loadImage("/imgs/ui/icon_policecar.png")
        ));
        uiObjects.put("icon_badguycar", new UIObject(
                new Shape(Shape.Type.BOX,
                        0, 0,
                        20, 20
                ),
                ImageUtils.loadImage("/imgs/ui/icon_badguycar.png")
        ));
        roadImages = new BufferedImage[2];
        roadImages[0] = ImageUtils.loadImage("/imgs/road/road_01.jpg");
        roadImages[1] = ImageUtils.loadImage("/imgs/road/road_02.jpg");
    }

    // Update game logic
    public void update() {
        moveObjects();
        checkCollision();
        checkCoolDown();
        checkPlayingMusic();
        rebuildGameObjects();
        if(badVehicles.isEmpty()) {
            setGameState(GameState.WIN);
        }
    }

    // Rebuild game objects from the object factory
    private synchronized void rebuildGameObjects() {
        while (!objectFactory.isEmpty()) {
            GameObject gameObject = objectFactory.build();
            if (gameObject instanceof Bullet) bullets.add((Bullet) gameObject);
            else if (gameObject instanceof CivVehicle) civVehicles.add((CivVehicle) gameObject);
        }
        badVehicles.removeIf(BadVehicle::isExpired);
        civVehicles.removeIf(CivVehicle::isExpired);
        bullets.removeIf(Bullet::isExpired);
    }

    // Check whether background music should change
    private void checkPlayingMusic() {
        boolean isActiveEnemy = badVehicles.stream().anyMatch(BadVehicle::isActive);
        if (isActiveEnemy != activeEnemy) musicPlaying = false;
        activeEnemy = isActiveEnemy;
        if (!isGameOver() && !musicPlaying) {
            if (activeEnemy) Sound.playMusic(Sound.MUSIC_GAME2);
            else Sound.playMusic(Sound.MUSIC_GAME1);
            musicPlaying = true;
        }
    }

    // Move game objects
    private void moveObjects() {
        playVehicle.updateInput(keyInput);
        if (playerShooting) {
            objectFactory.plan(playVehicle.shoot());
            Sound.playSound(Sound.SFX_SHOOT);
            playerShooting = false;
        }
        playVehicle.move();
        moveCivVehicles();
        moveBadVehicles();
        moveBullets();
    }

    // Check if the game is over
    public boolean isGameOver() {
        return gameState == GameState.WIN || gameState == GameState.LOSE;
    }

    // Check for collisions between game objects
    private void checkCollision() {
        // Check collisions between civilians and the player
        for (CivVehicle civVehicle : civVehicles) {
            if (civVehicle.checkCollision(playVehicle)) {
                playVehicle.damage(1);
                Sound.playSound(Sound.SFX_BREAK_CIV);
            }
            // Check collisions between civilians and enemies
            for (BadVehicle badVehicle : badVehicles) {
                if (!badVehicle.isBroken()) {
                    if (civVehicle.checkCollision(badVehicle)) {
                        Sound.playSound(Sound.SFX_BREAK_CIV);
                    }
                }
            }
        }

        // Check collisions involving bullets
        for (Bullet bullet : bullets) {
            if (bullet.getTeam() == GameObject.Team.FRIEND) {
                // Check collisions between friendly bullets and enemies
                for (BadVehicle badVehicle : badVehicles) {
                    if (bullet.checkCollision(badVehicle)) {
                        badVehicle.damage(1);
                        if (badVehicle.isBroken()) {
                            Sound.playSound(Sound.SFX_BREAK_NONCIV);
                        } else {
                            Sound.playSound(Sound.SFX_DAMAGE_NONCIV);
                        }
                    }
                }
                // Check collisions between friendly bullets and civilians
                for (CivVehicle civVehicle : civVehicles) {
                    if (civVehicle.checkCollision(bullet)) {
                        bullet.checkCollision(civVehicle);
                        Sound.playSound(Sound.SFX_BREAK_CIV);
                        playVehicle.damage(1);
                    }
                }
            } else {
                // Check collisions between enemy bullets and the player
                if (bullet.checkCollision(playVehicle)) {
                    playVehicle.damage(1);
                    Sound.playSound(Sound.SFX_DAMAGE_NONCIV);
                }
                // Check collisions between enemy bullets and civilians
                for (CivVehicle civVehicle : civVehicles) {
                    if (civVehicle.checkCollision(bullet)) {
                        bullet.checkCollision(civVehicle);
                        Sound.playSound(Sound.SFX_BREAK_CIV);
                    }
                }
            }
        }

        // Check if the player's vehicle is broken
        if (playVehicle.isBroken()) {
            setGameState(GameState.LOSE);
        }
    }
    // Enumeration representing game states
    public enum GameState {
        WIN, LOSE, PLAY, PAUSE
    }

    // Set the game state based on the specified state
    public void setGameState(GameState gameState_) {
        switch (gameState_) {
            case WIN:
                if (gameState != GameState.LOSE && gameState != GameState.WIN) {
                    // Display the "Win" banner and play victory sound
                    uiObjects.get("banner_win").setVisible(true);
                    Sound.playMusic(Sound.SFX_VICTORY, false);
                    startCooldown();
                    gameState = gameState_;
                }
                break;
            case LOSE:
                if (gameState != GameState.LOSE && gameState != GameState.WIN) {
                    // Display the "Lose" banner and play defeat sound
                    uiObjects.get("banner_lose").setVisible(true);
                    Sound.playMusic(Sound.SFX_DEFEAT, false);
                    startCooldown();
                    gameState = gameState_;
                }
                break;
            default:
                gameState = gameState_;
                break;
        }
    }

    // Start the cooldown period
    private void startCooldown() {
        cooldownFrames = COOLDOWN_SECONDS * FPS;
    }

    // Check if the cooldown period is over
    private void checkCoolDown() {
        if (cooldownFrames > 0) {
            cooldownFrames--;
        }
    }

    // Check if the cooldown period is over
    private boolean isCooldownOver() {
        return cooldownFrames <= 0;
    }

    // Move civilian vehicles and spawn new ones
    private void moveCivVehicles() {
        if (civilianSpawnDelay > 0) civilianSpawnDelay -= playVehicle.getSpeedX() / 5.0;
        for (CivVehicle civVehicle : civVehicles) {
            civVehicle.move(playVehicle);
        }
        if (!isGameOver() && civilianSpawnDelay <= 0) {
            // Spawn a new civilian vehicle
            CivVehicle civVehicle = new CivVehicle();
            civVehicle.getCenter().setLocation(
                    GameFrame.GAME_DIM.width + civVehicle.getShape().getWidth() / 2,
                    minY + R.nextInt(maxY - minY)
            );
            objectFactory.plan(civVehicle);
            civilianSpawnDelay = FPS *
                    (MIN_CIV_SPAWN_DELAY + R.nextDouble() * (MAX_CIV_SPAWN_DELAY - MIN_CIV_SPAWN_DELAY))
            ;
        }
    }

    // Move bad vehicles (enemy vehicles)
    private void moveBadVehicles() {
        boolean isActiveEnemy = false;
        for (BadVehicle badVehicle: badVehicles) {
            if (badVehicle.isActive()) {
                isActiveEnemy = true;
                badVehicle.move();
                Bullet bullet = badVehicle.shoot(playVehicle);
                if (bullet != null) {
                    Sound.playSound(Sound.SFX_SHOOT);
                    objectFactory.plan(bullet);
                }
            } else if (enemySpawnTime >= badVehicle.getSpawnDelaySeconds()*FPS) {
                // Spawn a new bad vehicle (enemy) if spawn time is reached
                badVehicle.spawn();
                isActiveEnemy = true;
            }
        }
        if (!isActiveEnemy && !isGameOver()) {
            enemySpawnTime++;
        }
    }

    // Move bullets in the game
    private void moveBullets() {
        for (Bullet bullet: bullets) {
            bullet.move();
        }
    }

    // Paint method to draw various game elements
    public void paint(Graphics g) {
        drawRoad(g);
        for (CivVehicle civVehicle: civVehicles) {
            civVehicle.draw(g);
        }
        for (BadVehicle badVehicle: badVehicles) {
            badVehicle.draw(g);
        }
        playVehicle.draw(g);
        for (Bullet bullet: bullets) {
            bullet.draw(g);
        }
        drawHealthBars(g);
        drawProgressBar(g);
        for (UIObject uiObject: uiObjects.values()) {
            if(uiObject.isVisible()) {
                uiObject.draw(g);
            }
        }
    }

    // Draw the road background
    private void drawRoad(Graphics g) {
//        g.setColor(Color.BLUE);
//        g.fillRect(0, 0, GAME_DIM.width, GAME_DIM.height);
//        g.setColor(Color.LIGHT_GRAY);
//        g.fillRect(
//                0,
//                (minY-ROAD_EXTENSION_Y),
//                GAME_DIM.width,
//                (maxY-minY+(ROAD_EXTENSION_Y*2)));
        roadTranslateX = (roadTranslateX + 100) % 400;
        g.drawImage(roadImages[level <= 3 ? 0 : 1],-roadTranslateX,0,null);
    }

    // Draw health bars for player and enemies
    private void drawHealthBars(Graphics g) {
        // Player's health bar
        g.setColor(Color.RED);
        g.fillRect(10, 10, 100, 20);
        g.setColor(Color.GREEN);
        g.fillRect(10, 10, 100*playVehicle.getHealthPoint()/playVehicle.getHealthPointMax(), 20);

        // Enemy health bars
        int i = 0;
        for (BadVehicle badVehicle: badVehicles) {
            if (badVehicle.isActive()) {
                g.setColor(Color.RED);
                g.fillRect(690, 10+(i*25), 100, 20);
                g.setColor(Color.GREEN);
                g.fillRect(690, 10+(i*25), 100*badVehicle.getHealthPoint()/badVehicle.getHealthPointMax(), 20);
                i++;
            }
        }
    }

    // Draw the progress bar indicating enemy spawn progress
    private void drawProgressBar(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(150, 10, 500, 10);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(150, 10, 500*enemySpawnTime/maxEnemySpawnTime, 10);
        g.drawImage(
                uiObjects.get("icon_policecar").getImage(),
                140+500*enemySpawnTime/maxEnemySpawnTime,
                5,20,20, null);
        for (BadVehicle badVehicle: badVehicles) {
            g.drawImage(
                    uiObjects.get("icon_badguycar").getImage(),
                    140+500*badVehicle.getSpawnDelaySeconds()*FPS/maxEnemySpawnTime,
                    5,20,20, null);
        }
    }

    // Run method for the animation thread
    @Override
    public void run() {
        animationThread.setPriority(Thread.MIN_PRIORITY);

        // Get the current time
        long startTime = System.currentTimeMillis();

        // Animate the scene in this thread
        while (!(isGameOver() && isCooldownOver()) && Thread.currentThread() == animationThread) {
            gamePanel.update(gamePanel.getGraphics());
            try {
                startTime += ANIMATION_DELAY;
                Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
            } catch (InterruptedException e) {}
        }
        // Exit the game, switch to the main menu
        exit();
    }

    // Exit the game and switch to the main menu
    public void exit() {
        for(Component c = this.gamePanel ; c != null; c = c.getParent()) {
            if (c instanceof GameFrame) {
                Sound.playMusic(Sound.MUSIC_MAINMENU);
                ((GameFrame)c).switchScreen(GameFrame.MENU_SCREEN);
                break;
            }
        }
    }

}
