import java.awt.image.BufferedImage;

public class DWT2 {
    // Embeds the given text in the given image and returns the modified image
    public BufferedImage embedText(BufferedImage image, String text) {
        // Perform the DWT on the input image
        int[][] dwt = performDWT(image);

        // Embed the text in the d1, d2, d3, and d4 coefficients
        dwt = embedTextInCoefficients(dwt, text);
        System.out.println(dwt[78][422]);

        // Return the modified image
        BufferedImage t = inverseDWT(dwt);
        // for(int i=0;i<image.getHeight();i++){
        //   for(int j=0;j<image.getWidth();j++){
        //     if(image.getRGB(j, i)!=t.getRGB(j, i) & i<85){
        //       System.out.println("i: "+i+" j: "+j);
        //       System.out.println("image: "+image.getRGB(j, i)+" t: "+t.getRGB(j, i));
        //     }
        //   }
        // }
        return t;
    }

    // Extract the text from the given image using DWT steganography
    public String extractText(BufferedImage image) {
        // Perform the DWT on the image to obtain the d1, d2, d3, and d4 coefficients
        int[][] dwt = performDWTExtract(image);

        // Extract the text from the d1, d2, d3, and d4 coefficients
        String text = extractTextFromCoefficients(dwt);

        // Convert the binary string into the original text
        // String text = "";
        // for (int i = 0; i < binaryText.length(); i += 8) {
        //     text += (char) Integer.parseInt(binaryText.substring(i, i + 8), 2);
        // }

        System.out.println(text);

        // Return the extracted text
        return text;
    }

    // Performs the DWT on the given image and returns the d1, d2, d3, and d4
    // coefficients
    public int[][] performDWT(BufferedImage image) {
        // Get the width and height of the image
        int width = image.getWidth();
        int height = image.getHeight();

        // Initialize the DWT array
        int[][] dwt = new int[height][width];

        // Loop over the image and perform the DWT
        int c=0;
        for (int i = 0; i < height - 1; i+=2) {
            for (int j = 0; j < width - 1; j+=2) {
                // Get the RGB value of the current pixel
                int p1 = image.getRGB(j, i);
                int p2 = image.getRGB(j + 1, i);
                int p3 = image.getRGB(j, i + 1);
                int p4 = image.getRGB(j + 1, i + 1);

                // Calculate the d1, d2, d3, and d4 coefficients
                // int d1 = (p1 + p2 + p3 + p4) / 4;
                // int d2 = (p1 - p2 + p3 - p4) / 4;
                // int d3 = (p1 + p2 - p3 - p4) / 4;
                // int d4 = (p1 - p2 - p3 + p4) / 4;

                // int avg = (p1 + p2 + p3 + p4) / 4;

                // // Calculate the difference between each pixel and the average
                // int d1 = p1 - avg;
                // int d2 = p2 - avg;
                // int d3 = p3 - avg;
                // int d4 = p4 - avg;
                if(i==78 && j==422){
                    System.out.println("p1: " + p1 + " p2: " + p2 + " p3: " + p3 + " p4: " + p4);
                }
                c++;

                
                int d1 = p1 + p3 + p4 - 2*p2;
                int d2 = p2 + p4 + p1 - 2*p3;
                int d3 = p3 + p1 + p2 - 2*p4;
                int d4 = p4 + p2 + p3 - 2*p1;

                if(c<40){
                  System.out.println("d1: " + d1 + " d2: " + d2 + " d3: " + d3 + " d4: " + d4);
                  System.out.println("p1: " + p1 + " p2: " + p2 + " p3: " + p3 + " p4: " + p4);
                  c++;
                }

                // Store the d1, d2, d3, and d4 coefficients in the DWT array
                dwt[i][j] = d1;
                dwt[i][j + 1] = d2;
                dwt[i + 1][j] = d3;
                dwt[i + 1][j + 1] = d4;
            }
        }

        // Return the DWT array
        return dwt;
    }

    // Perform the inverse DWT on the given dwt array
    public BufferedImage inverseDWT(int[][] dwt) {
        // Get the width and height of the dwt array
        int width = dwt[0].length;
        int height = dwt.length;

        // Create a new image with the same dimensions as the dwt array
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Loop over the dwt array and perform the inverse DWT
        int c=0;
        for (int i = 0; i < height - 1; i += 2) {
            for (int j = 0; j < width - 1; j += 2) {
                // Get the d1, d2, d3, and d4 coefficients
                // int d1 = dwt[i][j];
                // int d2 = dwt[i][j + 1];
                // int d3 = dwt[i + 1][j];
                // int d4 = dwt[i + 1][j + 1];

                // Calculate the RGB values of the current pixels
                // int p1 = (d1 + d2 + d3 + d4) / 4;
                // int p2 = (d1 - d2 + d3 - d4) / 4;
                // int p3 = (d1 + d2 - d3 - d4) / 4;
                // int p4 = (d1 - d2 - d3 + d4) / 4;

                // int avg = dwt[i][j];
                // int d1 = dwt[i][j + 1];
                // int d2 = dwt[i + 1][j];
                // int d3 = dwt[i + 1][j + 1];

                // // Calculate the original pixel values by adding the average and the differences
                // int p1 = avg + d1;
                // int p2 = avg + d2;
                // int p3 = avg + d3;
                // int p4 = avg + d1 + d2 + d3;

                int d1 = dwt[i][j];
                int d2 = dwt[i][j + 1];
                int d3 = dwt[i + 1][j];
                int d4 = dwt[i + 1][j + 1];

                
                int p1 = (d1 + d2 + d3) / 3;
                int p2 = (d2 + d3 + d4) / 3;
                int p3 = (d3 + d4 + d1) / 3;
                int p4 = (d4 + d1 + d2) / 3;
                if(p1%3==0){
                  int t=-(d1 + d2 + d3);
                  int near = p1-(p1%9);
                  if(t%3==0){
                    p1=t/3;
                  }
                }
                p1-=(-d1 - d2 - d3)%3;
                p2-=(-d2 - d3 - d4)%3;
                p3-=(-d3 - d4 - d1)%3;
                p4-=(-d4 - d1 - d2)%3;


                if(c<40){
                  System.out.println("d1: " + dwt[i][j] + " d2: " + dwt[i+1][j] + " d3: " + dwt[i][j+1] + " d4: " + dwt[i+1][j+1]);
                  System.out.println("p1: " + p1 + " p2: " + p2 + " p3: " + p3 + " p4: " + p4);
                  c++;
                }



                // Set the RGB values of the current pixels in the image
                image.setRGB(j, i, p1);
                image.setRGB(j + 1, i, p2);
                image.setRGB(j, i + 1, p3);
                image.setRGB(j + 1, i + 1, p4);
            }
        }

        // Return the reconstructed image
        return image;
    }

    public int[][] performDWTExtract(BufferedImage image) {
        // Get the width and height of the image
        int width = image.getWidth();
        int height = image.getHeight();

        // Initialize the DWT array
        int[][] dwt = new int[height][width];

        // Loop over the image and perform the DWT
        int c=0;
        for (int i = 0; i < height - 1; i+=2) {
            for (int j = 0; j < width - 1; j+=2) {
                // Get the RGB value of the current pixel
                int p1 = image.getRGB(j, i);
                int p2 = image.getRGB(j + 1, i);
                int p3 = image.getRGB(j, i + 1);
                int p4 = image.getRGB(j + 1, i + 1);
                if(p1%3!=0){
                  p1+=p1%3;
                  p1-=
                }
                if(p2%3!=0){
                  p2+=p2%3;
                }
                if(p3%3!=0){
                  p3+=p3%3;
                }

                // Calculate the d1, d2, d3, and d4 coefficients
                // int d1 = (p1 + p2 + p3 + p4) / 4;
                // int d2 = (p1 - p2 + p3 - p4) / 4;
                // int d3 = (p1 + p2 - p3 - p4) / 4;
                // int d4 = (p1 - p2 - p3 + p4) / 4;

                // int avg = (p1 + p2 + p3 + p4) / 4;

                // // Calculate the difference between each pixel and the average
                // int d1 = p1 - avg;
                // int d2 = p2 - avg;
                // int d3 = p3 - avg;
                // int d4 = p4 - avg;
                if(i==78 && j==422){
                    System.out.println("p1: " + p1 + " p2: " + p2 + " p3: " + p3 + " p4: " + p4);
                }
                c++;

                
                int d1 = p1 + p3 + p4 - 2*p2;
                int d2 = p2 + p4 + p1 - 2*p3;
                int d3 = p3 + p1 + p2 - 2*p4;
                int d4 = p4 + p2 + p3 - 2*p1;

                if(c<40){
                  System.out.println("d1: " + d1 + " d2: " + d2 + " d3: " + d3 + " d4: " + d4);
                  System.out.println("p1: " + p1 + " p2: " + p2 + " p3: " + p3 + " p4: " + p4);
                  c++;
                }

                // Store the d1, d2, d3, and d4 coefficients in the DWT array
                dwt[i][j] = d1;
                dwt[i][j + 1] = d2;
                dwt[i + 1][j] = d3;
                dwt[i + 1][j + 1] = d4;
            }
        }

        // Return the DWT array
        return dwt;
    }

    // Embed the given text in the given d1, d2, d3, and d4 coefficients
    public int[][] embedTextInCoefficients(int[][] dwt, String text) {
        // Convert the text into a binary string, where each character is represented by
        // 8 bits
        // String binaryText = "";
        // for (char c : text.toCharArray()) {
        //     binaryText += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
        // }
        String binaryText = "";
        for (char c : text.toCharArray()) {
          String binaryChar = Integer.toBinaryString(c);
          while (binaryChar.length() < 8) {
            // pad with 0s
            binaryChar = "0" + binaryChar;
          }
          binaryText += binaryChar;
        }
        System.out.println("binaryText: " + binaryText);

        // Loop over the d1, d2, d3, and d4 coefficients and embed each bit of the
        // binary string in the least significant bit of the coefficients

        // int index = 0;
        // for (int i = 0; i < dwt.length; i++) {
        //     for (int j = 0; j < dwt[i].length; j++) {
        //         // Check if there are still bits left to embed
        //         if (index < binaryText.length()) {
        //             // Get the current coefficient and the current bit
        //             int coefficient = dwt[i][j];
        //             int bit = Integer.parseInt(binaryText.charAt(index) + "");

        //             // Embed the bit in the least significant bit of the coefficient
        //             dwt[i][j] = (coefficient & ~1) | bit;
        //             index++;
        //         }
        //         // If there are no more bits left to embed, break out of the loop
        //         else {
        //             break;
        //         }
        //     }
        // }

        int messageIndex = 0;
        for (int i = 0; i < dwt.length; i++) {
          for (int j = 0; j < dwt[i].length; j++) {
            if (messageIndex >= binaryText.length()) {
              // message has been stored, so stop
              break;
            }
            // set the least significant bit of the coefficient to the value of the corresponding message bit
            int coefficient = (int) dwt[i][j];
            if (coefficient%2==0){
              if(binaryText.charAt(messageIndex)=='1') {
                coefficient |= 1; // set the least significant bit
              }
            }
            else{
              if(binaryText.charAt(messageIndex)=='0') {
                coefficient ^= 1; // set the least significant bit
              }
            }
            //System.out.println("Before: " + dwt[i][j]);
            dwt[i][j] = coefficient;
            //System.out.println("After: " + dwt[i][j]);
            messageIndex++;
          }
        }

        // Return the modified d1, d2, d3, and d4 coefficients
        return dwt;
    }

    // Extract the text from the given d1, d2, d3, and d4 coefficients
    public String extractTextFromCoefficients(int[][] dwt) {
        // Loop over the d1, d2, d3, and d4 coefficients and extract the bits from the
        // least significant bit of the coefficients
        String binaryText = "";
        // System.out.println("binaryText: " + binaryText);
        String text = "";
        int bits = 0;
        int s=0;
        for (int i = 0; i < dwt.length; i++) {
            for (int j = 0; j < dwt[i].length; j++) {
                // Get the current coefficient and the least significant bit

                int coefficient = dwt[i][j];
                bits++;

                // Add the bit to the binary string
                // binaryText += bit;
                System.out.println(coefficient);
                binaryText += ((coefficient & 1) == 1) ? '1' : '0';
                if (bits==8){
                  if (binaryText.equals("00000000")){
                    System.out.println("text: " + text);
                    return text;
                  }
                  System.out.println("Binary: " + binaryText);
                  char c = (char) Integer.parseInt(binaryText, 2);
                  text += c;
                  binaryText = "";
                  bits = 0;
                }
                if (s==32){
                  return text;
                }
                s++;
            }
        }
        // System.out.println("binaryText: " + binaryText);

        // Return the extracted binary string
        return text;
    }
}



// public class DWT2 {
//     // Embed the given text in the given image
//     public BufferedImage embedText(BufferedImage image, String text) {
//         // Perform the DWT on the image to generate the DWT coefficients
//         int[][] dwt = performDWT(image);

//         // Embed the text in the DWT coefficients
//         dwt = embedTextInCoefficients(dwt, text);

//         // Perform the iDWT on the modified DWT coefficients to generate the stego-image
//         // with the hidden text
//         BufferedImage stegoImage = inverseDWT(dwt);

//         return stegoImage;
//     }

//     // Extract the hidden text from the given stego-image
//     public String extractText(BufferedImage stegoImage) {
//         // Perform the DWT on the stego-image to generate the DWT coefficients
//         int[][] dwt = performDWT(stegoImage);

//         // Extract the text from the DWT coefficients
//         String extractedText = extractTextFromCoefficients(dwt);

//         return extractedText;
//     }

//     // Perform the discrete wavelet transform (DWT) on the given image
//     public int[][] performDWT(BufferedImage image) {
//         // Get the dimensions of the image
//         int width = image.getWidth();
//         int height = image.getHeight();

//         // Initialize the DWT array with the same dimensions as the image
//         int[][] dwt = new int[height][width];

//         // Loop over the image and perform the DWT on each pixel
//         for (int i = 0; i < height - 1; i++) {
//             for (int j = 0; j < width - 1; j++) {
//                 // Get the 4 neighboring pixels of the current pixel
//                 int p1 = image.getRGB(j, i);
//                 int p2 = image.getRGB(j + 1, i);
//                 int p3 = image.getRGB(j, i + 1);
//                 int p4 = image.getRGB(j + 1, i + 1);

//                 // Calculate the average of the 4 neighboring pixels
//                 int avg = (p1 + p2 + p3 + p4) / 4;

//                 // Calculate the difference between each pixel and the average
//                 int d1 = p1 - avg;
//                 int d2 = p2 - avg;
//                 int d3 = p3 - avg;
//                 int d4 = p4 - avg;

//                 // Store the calculated values in the DWT array
//                 dwt[i][j] = avg;
//                 dwt[i][j + 1] = d1;
//                 dwt[i + 1][j] = d2;
//                 dwt[i + 1][j + 1] = d3;
//             }
//         }

//         return dwt;
//     }

//     // Perform the inverse discrete wavelet transform (IDWT) on the given DWT array
//     public BufferedImage inverseDWT(int[][] dwt) {
//         // Get the dimensions of the DWT array
//         int height = dwt.length;
//         int width = dwt[0].length;

//         // Initialize the output image with the same dimensions as the DWT array
//         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

//         // Loop over the DWT array and perform the IDWT on each pixel
//         for (int i = 0; i < height - 1; i++) {
//             for (int j = 0; j < width - 1; j++) {
//                 // Get the 4 neighboring pixels of the current pixel
//                 int avg = dwt[i][j];
//                 int d1 = dwt[i][j + 1];
//                 int d2 = dwt[i + 1][j];
//                 int d3 = dwt[i + 1][j + 1];

//                 // Calculate the original pixel values by adding the average and the differences
//                 int p1 = avg + d1;
//                 int p2 = avg + d2;
//                 int p3 = avg + d3;
//                 int p4 = avg + d1 + d2 + d3;

//                 // Store the calculated pixel values in the output image
//                 image.setRGB(j, i, p1);
//                 image.setRGB(j + 1, i, p2);
//                 image.setRGB(j, i + 1, p3);
//                 image.setRGB(j + 1, i + 1, p4);
//             }
//         }

//         return image;
//     }

//     // Embed the given text in the given DWT array
//     public int[][] embedTextInCoefficients(int[][] dwt, String text) {
//         // Get the dimensions of the DWT array
//         int height = dwt.length;
//         int width = dwt[0].length;

//         // Initialize the index variables
//         int i = 0;
//         int j = 0;

//         // Loop over the characters of the text to be embedded
//         for (int k = 0; k < text.length(); k++) {
//             // Get the current character
//             char c = text.charAt(k);

//             // Loop over the bits of the current character
//             for (int bit = 0; bit < 8; bit++) {
//                 // Get the 4 neighboring pixels of the current pixel
//                 int avg = dwt[i][j];
//                 int d1 = dwt[i][j + 1];
//                 int d2 = dwt[i + 1][j];
//                 int d3 = dwt[i + 1][j + 1];

//                 // Calculate the average value of the 4 neighboring pixels
//                 int avg2 = (d1 + d2 + d3 + avg) / 4;

//                 // Check if the current bit is 1 or 0
//                 if (((c >> bit) & 1) == 1) {
//                     // If the current bit is 1, calculate the new pixel values by adding the average
//                     // value
//                     int newAvg = avg + avg2;
//                     int newD1 = d1 + avg2;
//                     int newD2 = d2 + avg2;
//                     int newD3 = d3 + avg2;

//                     // Store the new pixel values in the DWT array
//                     dwt[i][j] = newAvg;
//                     dwt[i][j + 1] = newD1;
//                     dwt[i + 1][j] = newD2;
//                     dwt[i + 1][j + 1] = newD3;
//                 } else {
//                     // If the current bit is 0, calculate the new pixel values by subtracting the
//                     // average value
//                     int newAvg = avg - avg2;
//                     int newD1 = d1 - avg2;
//                     int newD2 = d2 - avg2;
//                     int newD3 = d3 - avg2;

//                     // Store the new pixel values in the DWT array
//                     dwt[i][j] = newAvg;
//                     dwt[i][j + 1] = newD1;
//                     dwt[i + 1][j] = newD2;
//                     dwt[i + 1][j + 1] = newD3;
//                 }

//                 // Increment the index variables to move to the next pixel
//                 i++;
//                 if (i >= height - 1) {
//                     i = 0;
//                     j += 2;
//                 }
//             }
//         }
//         return dwt;
//     }

//     // Extract the embedded text from the given DWT array
//     public String extractTextFromCoefficients(int[][] dwt) {
//         // Get the dimensions of the DWT array
//         int height = dwt.length;
//         int width = dwt[0].length;

//         // Initialize the output string
//         String text = "";

//         // Initialize the index variables
//         int i = 0;
//         int j = 0;

//         // Loop until the end of the DWT array is reached
//         while (i < height - 1 && j < width - 1) {
//             // Initialize the current character
//             char c = 0;

//             // Loop over the bits of the current character
//             for (int bit = 0; bit < 8; bit++) {
//                 // Get the 4 neighboring pixels of the current pixel
//                 int avg = dwt[i][j];
//                 int d1 = dwt[i][j + 1];
//                 int d2 = dwt[i + 1][j];
//                 int d3 = dwt[i + 1][j + 1];

//                 // Calculate the average value of the 4 neighboring pixels
//                 int avg2 = (d1 + d2 + d3 + avg) / 4;

//                 // Calculate the bit value by checking if the average is greater than or equal
//                 // to the original average value
//                 int b = (avg2 >= avg) ? 1 : 0;

//                 // Set the current bit position of the current character to the calculated bit
//                 // value
//                 c |= b << bit;

//                 // Increment the index variables to move to the next pixel
//                 i++;
//                 if (i >= height - 1) {
//                     i = 0;
//                     j += 2;
//                 }
//             }

//             // Add the current character to the output string
//             text += c;
//         }

//         return text;
//     }

// }


// import java.io.InputStream;

// public class DWT2 {

//   // Embeds a message into an image using the DWT
//   public BufferedImage embedText(BufferedImage image, String message){
//     // Perform the DWT on the image
//     BufferedImage dwtImage = dwt(image);

//     // Hide the message in the DWT coefficients of the image
//     hideMessage(dwtImage, message);
//     return dwtImage;
//   }

//   // Extracts a message from an image using the DWT
//   public String extractText(BufferedImage image){
//     // Perform the DWT on the image
//     BufferedImage dwtImage = dwt(image);

//     // Retrieve the hidden message from the DWT coefficients of the image
//     return retrieveMessage(dwtImage);
//   }

//   // Performs the DWT on an image
// private BufferedImage dwt(BufferedImage image) {
//     // Get the width and height of the image
//     int width = image.getWidth();
//     int height = image.getHeight();
  
//     // Create a new image for the DWT coefficients
//     BufferedImage dwtImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  
//     // Perform the DWT on the image using a Haar wavelet
//     for (int i = 0; i < width; i++) {
//       for (int j = 0; j < height; j++) {
//         // Get the pixel value at (i, j)
//         int pixel = image.getRGB(i, j);
  
//         // Separate the pixel into its RGB color channels
//         int r = (pixel >> 16) & 0xff;
//         int g = (pixel >> 8) & 0xff;
//         int b = pixel & 0xff;
  
//         // Perform the DWT on each color channel
//         int[] dwtR = dwt1D(r);
//         int[] dwtG = dwt1D(g);
//         int[] dwtB = dwt1D(b);
  
//         // Combine the DWT coefficients of the three color channels into a single pixel
//         int dwtPixel = (dwtR[0] << 16) | (dwtG[0] << 8) | dwtB[0];
  
//         // Set the DWT coefficient at (i, j) in the new image
//         dwtImage.setRGB(i, j, dwtPixel);
//       }
//     }
  
//     return dwtImage;
//   }
  
//   // Performs the 1D DWT on a 1D array of values using a Haar wavelet
//   private int[] dwt1D(int[] values) {
//     int n = values.length;
//     int[] dwt = new int[n];
  
//     for (int i = 0; i < n; i++) {
//       int average = (values[i] + values[i + 1]) / 2;
//       int detail = values[i] - average;
//       dwt[i] = average;
//       dwt[i + 1] = detail;
//     }
  
//     return dwt;
//   }
  

//   // Hides a message in the DWT coefficients of an image
//   private void hideMessage(BufferedImage image, String message) {
//     // TODO: Implement the message hiding here
//   }

//   // Retrieves a hidden message from the DWT coefficients of an image
//   private String retrieveMessage(BufferedImage image) {
//     // TODO: Implement the message retrieval here
//   }
// }
