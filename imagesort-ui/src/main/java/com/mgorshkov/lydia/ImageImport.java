package com.mgorshkov.lydia;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * @author Maxim Gorshkov <maxim.gorshkov<at>savoirfairelinux.com>
 */
public class ImageImport {

    public ImageImport(File f){
        try
        {
            InputStream is = new BufferedInputStream(new FileInputStream(f.getPath()));
            BufferedImage image = ImageIO.read(is);
            marchThroughImage(image);
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    private static void marchThroughImage(BufferedImage image) throws IOException {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage tmpImg = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);

        int[][] b = new int[w][h];

        for(int i = 0; i<w; i++){
            for(int j = 0; j<h; j++){
                b[i][j] = image.getRGB(i, j);
            }
        }



//        for(int i = 0; i<b.length; i++){
//            for(int j = 0; j<b[i].length; j++){
//                System.out.println(b[i][j]);
//            }
//        }

        for(int i = 0; i<w; i++){
            Arrays.sort(b[i]);
        }

        for(int i = 0; i<w; i++){
            for(int j = 0; j<h; j++){
                tmpImg.setRGB(i, j, b[i][j]);
            }
        }

            ImageIO.write(tmpImg, "png", new File(
                    "output.png"));
    }
}
