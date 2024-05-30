package edu.uchicago.mauliafirmansyah.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

// Utility class for loading images, extracted from Sprite class
public class ImageUtils {

    // Method to load a BufferedImage from the specified imagePath
    public static BufferedImage loadImage(String imagePath) {
        BufferedImage bufferedImage;
        try {
            // Attempt to read the image from the resource stream
            bufferedImage = ImageIO.read(Objects.requireNonNull(ImageUtils.class.getResourceAsStream(imagePath)));
        }
        catch (IOException e) {
            // Print the stack trace if an exception occurs during image loading
            e.printStackTrace();
            bufferedImage = null; // Set bufferedImage to null in case of an exception
        }
        // Return the loaded BufferedImage (or null if an exception occurred)
        return bufferedImage;
    }
}
