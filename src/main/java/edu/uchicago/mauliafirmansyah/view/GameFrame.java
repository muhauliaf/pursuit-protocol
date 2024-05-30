package edu.uchicago.mauliafirmansyah.view;

import edu.uchicago.mauliafirmansyah.controller.Game;
import edu.uchicago.mauliafirmansyah.controller.Sound;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class GameFrame extends JFrame {
    // Dimensions for the game window
    public static final Dimension GAME_DIM = new Dimension(800, 600);

    // Constants for different screen modes
    public static final int MENU_SCREEN = 0;
    public static final int LEVEL_SCREEN = 1;
    public static final int HELP_SCREEN = 2;
    public static final int GAME_SCREEN = 3;

    // Panels for different screens
    private JPanel currentPanel, menuPanel, helpPanel, levelPanel;

    // Current level in the game
    private int currentLevel;

    // Constructor initializes the frame and sets up the main menu music
    public GameFrame() {
        super();
        currentLevel = 1;
        initPanels();
        initFrame();
        Sound.playMusic(Sound.MUSIC_MAINMENU);
    }

    // Initialize different properties of the frame
    private void initFrame() {
        // Set the default close operation of the GameFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the GameFrame
        setSize(GameFrame.GAME_DIM);

        // Set the title of the GameFrame
        setTitle("Pursuit Protocol");

        // Switch the screen to the main menu
        switchScreen(GameFrame.MENU_SCREEN);

        // Make the GameFrame visible
        setVisible(true);
    }

    // Initialize different panels for menu, help, and level selection
    private void initPanels() {
        String[] menuTexts = {"Play", "Help", "Quit"};
        menuPanel = new MenuPanel(menuTexts, "/imgs/ui/bg_mainmenu.jpg") {
            @Override
            public void buttonPressed(String text) {
                switch (text) {
                    case "Play":
                        GameFrame.this.switchScreen(LEVEL_SCREEN);
                        break;
                    case "Help":
                        GameFrame.this.switchScreen(HELP_SCREEN);
                        break;
                    case "Quit":
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        };

        levelPanel = new LevelPanel(9, "Level", "Back", "/imgs/ui/bg_levelmenu.jpg") {
            @Override
            public void levelButtonPressed(int level) {
                currentLevel = level;
                GameFrame.this.switchScreen(GAME_SCREEN);
            }

            @Override
            public void backButtonPressed() {
                GameFrame.this.switchScreen(MENU_SCREEN);
            }
        };

        helpPanel = new HelpPanel("Back", "/imgs/ui/bg_helpmenu.jpg") {
            @Override
            public void backButtonPressed() {
                GameFrame.this.switchScreen(MENU_SCREEN);
            }
        };
    }

    // Switch the screen based on the provided screen mode
    public void switchScreen(int screenMode) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        switch (screenMode) {
            case MENU_SCREEN:
                currentPanel = menuPanel;
                break;
            case HELP_SCREEN:
                currentPanel = helpPanel;
                break;
            case LEVEL_SCREEN:
                currentPanel = levelPanel;
                break;
            case GAME_SCREEN:
                currentPanel = new Game(currentLevel).getGamePanel();
                break;
            default:
                break;
        }
        add(currentPanel);
        revalidate();
        repaint();
        currentPanel.setFocusable(true);
        currentPanel.requestFocusInWindow();
    }
}
