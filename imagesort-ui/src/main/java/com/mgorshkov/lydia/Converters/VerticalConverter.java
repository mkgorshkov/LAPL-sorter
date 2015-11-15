package com.mgorshkov.lydia.Converters;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * @author Maxim Gorshkov <maxim.gorshkov<at>savoirfairelinux.com>
 */
public class VerticalConverter implements ImageConvertInterface {
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

        for(int i = 0; i<imageWidth; i++){
            for(int j = 0; j<imageHeight; j++){
                b[i][j] = inputImage.getRGB(i, j);
            }
        }

        for(int i = 0; i<imageWidth; i++){
            Arrays.sort(b[i]);
        }

        for(int i = 0; i<imageWidth; i++){
            for(int j = 0; j<imageHeight; j++){
                tmpImg.setRGB(i, j, b[i][j]);
            }
        }

        return tmpImg;
    }
}
