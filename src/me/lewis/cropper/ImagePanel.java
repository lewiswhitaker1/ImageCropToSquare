package me.lewis.cropper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        double widthScale = (double) panelWidth / imageWidth;
        double heightScale = (double) panelHeight / imageHeight;
        double scale = Math.min(widthScale, heightScale);
        int scaledWidth = (int) (imageWidth * scale);
        int scaledHeight = (int) (imageHeight * scale);
        int x = (panelWidth - scaledWidth) / 2;
        int y = (panelHeight - scaledHeight) / 2;
        g.drawImage(image, x, y, scaledWidth, scaledHeight, null);
    }
}