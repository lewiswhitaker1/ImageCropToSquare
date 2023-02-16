package me.lewis.cropper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Cropper {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java ImageCropper <image file>");
            System.exit(1);
        }

        try {
            BufferedImage image = openImage(new File(args[0]));
            ImageCropper cropper = new ImageCropper(image, true);
            JFrame frame = new JFrame("Cropper");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(cropper);
            frame.pack();
            frame.setVisible(true);
            registerIcons(frame);
            frame.toFront();
            frame.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void registerIcons(JFrame frame)
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        ArrayList<Image> imageList = new ArrayList<Image>();
        java.net.URL url16 = ClassLoader.getSystemResource("me/lewis/cropper/resources/16.png");
        Image img16 = kit.createImage(url16);
        java.net.URL url32 = ClassLoader.getSystemResource("me/lewis/cropper/resources/32.png");
        Image img32 = kit.createImage(url32);
        java.net.URL url64 = ClassLoader.getSystemResource("me/lewis/cropper/resources/64.png");
        Image img64 = kit.createImage(url64);
        java.net.URL url128 = ClassLoader.getSystemResource("me/lewis/cropper/resources/128.png");
        Image img128 = kit.createImage(url128);
        imageList.add(img16);
        imageList.add(img32);
        imageList.add(img64);
        imageList.add(img128);
        frame.setIconImages(imageList);
    }

    public static BufferedImage openImage(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        int width = img.getWidth();
        int height = img.getHeight();
        double aspectRatio = (double) width / height;
        if (width > height && width > 1000) {
            width = 1000;
            height = (int) (width / aspectRatio);
        } else if (height > 1000) {
            height = 1000;
            width = (int) (height * aspectRatio);
        }
        Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledBI = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaledBI.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return scaledBI;
    }
}