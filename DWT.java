import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;

// import javax.imageio.ImageIO;
// import java.util.Arrays;

public class DWT {
    // Apply the DWT to the input image and return the wavelet coefficients
    public static double[][] dwt(BufferedImage image) {
        // Create a 2D array to hold the wavelet coefficients
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] waveletCoefficients = new double[width][height];

        // Apply the DWT to the input image to obtain the wavelet coefficients
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Get the pixel at (i, j)
                int pixel = image.getRGB(i, j);

                // Extract the red, green, and blue components of the pixel
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                // Compute the average and difference values for the red, green, and blue components
                int ra = (r + g + b) / 3;
                int rd = (r - g) / 2;
                int ga = (r + g - b) / 3;
                int gd = (g - b) / 2;
                int ba = (r - g + b) / 3;
                int bd = (r + g + 2 * b) / 4;

                // Store the average and difference values in the wavelet coefficients array
                waveletCoefficients[i][j] = ra;
                if(i+width/2 < width) {
                    waveletCoefficients[i + width / 2][j] = rd;
                }
                if(j+height/2 < height) {
                    waveletCoefficients[i][j + height / 2] = ga;
                }
                if (i+width/2 < width && j+height/2 < height) {
                    waveletCoefficients[i + width / 2][j + height / 2] = gd;
                }
                if (j+height < height) {
                    waveletCoefficients[i][j + height] = ba;
                }
                if (i+width/2 < width && j+height < height) {
                    waveletCoefficients[i + width / 2][j + height] = bd;
                }
            }
        }

        return waveletCoefficients;
    }

    // Apply the IDWT to the wavelet coefficients and return the output image
    public static BufferedImage idwt(double[][] waveletCoefficients) {
        // Create a new BufferedImage to hold the output image
        int width = waveletCoefficients.length;
        int height = waveletCoefficients[0].length;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Apply the IDWT to the wavelet coefficients to generate the output image
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Get the wavelet coefficients at the current position and its neighbors
                double c00 = waveletCoefficients[(i + width - 1) % width][(j + height - 1) % height];
                double c01 = waveletCoefficients[(i + width - 1) % width][j];
                double c02 = waveletCoefficients[(i + width - 1) % width][(j + 1) % height];
                double c10 = waveletCoefficients[i][(j + height - 1) % height];
                double c11 = waveletCoefficients[i][j];
                double c12 = waveletCoefficients[i][(j + 1) % height];
                double c20 = waveletCoefficients[(i + 1) % width][(j + height - 1) % height];
                double c21 = waveletCoefficients[(i + 1) % width][j];
                double c22 = waveletCoefficients[(i + 1) % width][(j + 1) % height];

                // Apply the IDWT using the scaling and wavelet functions of the Haar wavelet transform
                int r = (int) (c11 + (c00 + c22) / 2 + (c10 + c21) / 2);
                int g = (int) (c11 + (c02 + c20) / 2 + (c01 + c12) / 2);
                int b = (int) (c11 + (c00 + c22) / 2 - (c10 + c21) / 2);

                // Clamp the red, green, and blue values to the valid range [0, 255]
                r = Math.max(0, Math.min(255, r));
                g = Math.max(0, Math.min(255, g));
                b = Math.max(0, Math.min(255, b));

                // Create a new pixel from the red, green, and blue values
                int pixel = (r << 16) | (g << 8) | b;

                // Set the pixel in the output image
                outputImage.setRGB(i, j, pixel);
            }
        }

        return outputImage;
    }

    // Embed the message in the input image and return the output image
    public static BufferedImage embed(BufferedImage image, String message) {
        // Apply the DWT to the input image to obtain the wavelet coefficients
        double[][] waveletCoefficients = dwt(image);

        // Convert the message string to a byte array
        byte[] messageBytes = message.getBytes();

        // Modify the wavelet coefficients by replacing some of the least significant bits with the bits of the message
        for (int i = 0; i < waveletCoefficients.length; i++) {
            for (int j = 0; j < waveletCoefficients[0].length; j++) {
                // Get the wavelet coefficient at (i, j)
                double waveletCoefficient = waveletCoefficients[i][j];

                // Convert the wavelet coefficient to an integer
                int waveletCoefficientInt = (int) waveletCoefficient;

                // Replace the least significant bit of the wavelet coefficient with the next bit of the message
                waveletCoefficientInt = waveletCoefficientInt & ~1 | messageBytes[i * waveletCoefficients[0].length + j] & 1;

                // Convert the modified wavelet coefficient back to a double and store it in the wavelet coefficients array
                waveletCoefficients[i][j] = (double) waveletCoefficientInt;
            }
        }

        // Apply the IDWT to the modified wavelet coefficients to generate the output image
        BufferedImage outputImage = idwt(waveletCoefficients);

        return outputImage;
    }
    
    // Extract the message from the output image
    public static String extract(BufferedImage image) {
        // Apply the DWT to the output image to obtain the wavelet coefficients
        double[][] waveletCoefficients = dwt(image);

        // Retrieve the hidden message by extracting the bits that were replaced in the wavelet coefficients during the embed process
        byte[] messageBytes = new byte[waveletCoefficients.length * waveletCoefficients[0].length];
        for (int i = 0; i < waveletCoefficients.length; i++) {
            for (int j = 0; j < waveletCoefficients[0].length; j++) {
                // Get the wavelet coefficient at (i, j)
                double waveletCoefficient = waveletCoefficients[i][j];

                // Extract the least significant bit of the wavelet coefficient and store it in the message byte array
                messageBytes[i * waveletCoefficients[0].length + j] = (byte) ((int)waveletCoefficient & 1);
            }
        }

        // Convert the message byte array to a string
        String message = new String(messageBytes);

        return message;
    } 
}

