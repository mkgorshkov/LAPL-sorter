package com.mgorshkov.lydia.Converters;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Maxim Gorshkov <maxim.gorshkov<at>savoirfairelinux.com>
 */
public class RandomConverter implements ImageConvertInterface {
    private BufferedImage inputImage;

    int imageWidth, imageHeight;

    @Override
    public void createImage(BufferedImage image) {
        inputImage = image;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
    }

    @Override
    public BufferedImage returnConvertedImage() {

        BufferedImage tmpImg = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB);

        int[][] b = new int[imageWidth][imageHeight];

        Random r = new Random();

        for(int i = 0; i<imageWidth; i++){
            for(int j = 0; j<imageHeight; j++){
                b[i][j] = inputImage.getRGB(i, j);
            }
        }

        for(int i = 0; i<imageWidth; i++){
            for(int j = 0; j<imageHeight; j++){
                int x = r.nextInt(imageHeight);
                int y = r.nextInt(imageWidth);
                tmpImg.setRGB(i, j, b[y][x]);
            }
        }

        return tmpImg;
    }
}
