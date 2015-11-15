package com.mgorshkov.lydia;


import com.mgorshkov.lydia.Converters.HorizontalConverter;
import com.mgorshkov.lydia.Converters.RandomConverter;
import com.mgorshkov.lydia.Converters.VerticalConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author Maxim Gorshkov <maxim.gorshkov<at>savoirfairelinux.com>
 */
public class ImageImport {

    final static String[] TYPES = {"Horizontal", "Vertical", "Random"};

    public ImageImport(File f, String input){
        try
        {
            InputStream is = new BufferedInputStream(new FileInputStream(f.getPath()));
            BufferedImage image = ImageIO.read(is);
            BufferedImage tmpImg = null;

            if(input.equals(TYPES[0])){
                HorizontalConverter horizontalConverter = new HorizontalConverter();
                horizontalConverter.createImage(image);
                tmpImg = horizontalConverter.returnConvertedImage();
            }else if(input.equals(TYPES[1])){
                VerticalConverter verticalConverter = new VerticalConverter();
                verticalConverter.createImage(image);
                tmpImg = verticalConverter.returnConvertedImage();
            }else{
                RandomConverter randomConverter = new RandomConverter();
                randomConverter.createImage(image);
                tmpImg = randomConverter.returnConvertedImage();
            }

            ImageIO.write(tmpImg, "png", new File("output.png"));

        } catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

//    /*
//    Horizontal
//     */
//    private static void marchThroughImage(BufferedImage image) throws IOException{
//        int w = image.getWidth();
//        int h = image.getHeight();
//
//
//
//
//    }
//
//    /*
//    Vertical
//     */
//    private static void marchThroughImage(BufferedImage image) throws IOException {
//        int w = image.getWidth();
//        int h = image.getHeight();
//
//
//
//            ImageIO.write(tmpImg, "png", new File(
//                    "output.png"));
//    }
}
