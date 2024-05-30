package edu.uchicago.mauliafirmansyah.controller;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// Utility class for handling sound effects and music
public class Sound {

    // Executor for managing sound-related threads, extracted from asteroid CommandCenter class
    private static final ThreadPoolExecutor SOUND_EXECUTOR = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    // Flag indicating whether sound has been initialized
    public static boolean initialized = false;

    // Current music clip being played
    public static Clip currentMusicClip;

    // Sound effect and music file names
    public final static String MUSIC_MAINMENU = "music_mainmenu.wav";
    public final static String MUSIC_GAME1 = "music_game1.wav";
    public final static String MUSIC_GAME2 = "music_game2.wav";
    public final static String SFX_BUTTON = "sfx_button.wav";
    public final static String SFX_SHOOT = "sfx_shoot.wav";
    public final static String SFX_BREAK_CIV = "sfx_break_civ.wav";
    public final static String SFX_BREAK_NONCIV = "sfx_break_nonciv.wav";
    public final static String SFX_DAMAGE_NONCIV = "sfx_damage_nonciv.wav";
    public final static String SFX_VICTORY = "sfx_victory.wav";
    public final static String SFX_DEFEAT = "sfx_defeat.wav";

    // Play music with the option to loop
    public static void playMusic(String soundFileName, boolean loop) {
        if (currentMusicClip != null) {
            currentMusicClip.stop();
            currentMusicClip.close();
        }
        currentMusicClip = generateClip(soundFileName);
        if (loop) currentMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        currentMusicClip.start();
    }

    // Play music with looping enabled by default
    public static void playMusic(String soundFileName) {
        playMusic(soundFileName, true);
    }

    // Play a sound effect
    public static void playSound(String soundFileName) {
        generateClip(soundFileName).start();
    }

    // Generate a Clip from a sound file
    public static Clip generateClip(String fileName) {
        Clip clip = null;
        try {
            // Construct the relative path to the sound file
            String relativePath = "/sounds/" + fileName;
            InputStream audioSrc = Sound.class.getResourceAsStream(relativePath);

            // Check if the sound file exists
            if (audioSrc == null)
                throw new IOException("No such sound file exists at " + relativePath);

            // Wrap the audio source in a buffered stream
            InputStream bufferedIn = new BufferedInputStream(audioSrc);

            // Create an AudioInputStream from the buffered stream
            AudioInputStream aisStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Create a Clip from the AudioInputStream
            clip = AudioSystem.getClip();
            clip.open(aisStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return clip;
    }
}
