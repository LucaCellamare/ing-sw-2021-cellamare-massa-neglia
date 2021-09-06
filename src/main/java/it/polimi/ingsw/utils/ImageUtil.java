package it.polimi.ingsw.utils;

import it.polimi.ingsw.server.Model.Enums.TypeEnum;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

public class ImageUtil {
    public static final EnumMap<TypeEnum, Color> colorMap = new EnumMap<>(TypeEnum.class) {{
        put(TypeEnum.GREEN, new Color(14, 125, 47));
        put(TypeEnum.BLUE, new Color(66, 135, 245));
        put(TypeEnum.PURPLE, new Color(132, 54, 173));
        put(TypeEnum.YELLOW, new Color(248, 250, 135));
    }};

    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
}
