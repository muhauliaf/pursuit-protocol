package edu.uchicago.mauliafirmansyah.controller;

import lombok.Data;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Abstract class for handling keyboard input, extracted from asteroid Game class
@Data
public abstract class KeyInput implements KeyListener {

    // Constants representing key codes for specific actions
    public static final int
            LEFT = 37,      // Left arrow key
            RIGHT = 39,     // Right arrow key
            UP = 38,        // Up arrow key
            DOWN = 40,      // Down arrow key
            SPACE = 32      // Space bar key
                    ;

    // Array of key codes corresponding to specific actions
    public static final int[] keyValues = {UP, DOWN, LEFT, RIGHT, SPACE};

    // Array to store the state of each key (pressed or not)
    private boolean[] keyPress;

    // Constructor to initialize the keyPress array
    public KeyInput() {
        keyPress = new boolean[keyValues.length];
    }

    // Check if a specific key is currently pressed
    public boolean isKeyPress(int keyCode) {
        for (int i = 0; i < keyValues.length; i++) {
            if (keyCode == keyValues[i] && keyPress[i]) {
                return true;
            }
        }
        return false;
    }

    // Handler for keyTyped event (not used in this implementation)
    @Override
    public void keyTyped(KeyEvent e) {
        // Implementation not needed for this abstract class
    }

    // Handler for keyPressed event
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (int i = 0; i < keyValues.length; i++) {
            if (keyCode == keyValues[i]) {
                keyPress[i] = true;
                return;
            }
        }
    }

    // Handler for keyReleased event
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (int i = 0; i < keyValues.length; i++) {
            if (keyCode == keyValues[i]) {
                keyPress[i] = false;
                keyCodePressed(keyCode); // Notify subclass about key release
                return;
            }
        }
    }

    // Abstract method to be implemented by subclasses for additional key press handling
    public abstract void keyCodePressed(int keyCode);
}
