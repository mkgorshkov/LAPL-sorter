package com.mgorshkov.lydia.Converters;

import java.awt.image.BufferedImage;

/**
 * @author Maxim Gorshkov <maxim.gorshkov<at>savoirfairelinux.com>
 */
public interface ImageConvertInterface {
    void createImage(BufferedImage image);
    BufferedImage returnConvertedImage();
}
